package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
class CmcCoinsRepo implements CoinsRepo {

    private final CmcApi api;

    private final LoftDatabase db;

    private final ExecutorService executor;

    @Inject
    public CmcCoinsRepo(CmcApi api, LoftDatabase db, ExecutorService executor) {
        this.api = api;
        this.db = db;
        this.executor = executor;
    }

    @NonNull
    @Override
    public LiveData<List<Coin>> listings(@NonNull Query query) {
        fetchFromNetworkIfNecessary(query);
        return fetchFromDb(query);
    }

    private LiveData<List<Coin>> fetchFromDb(Query query) {
        LiveData<List<RoomCoin>> coins;
        if (query.sortBy() == SortBy.PRICE) {
            coins = db.coins().fetchAllSortByPrice();
        } else {
            coins = db.coins().fetchAllSortByRank();
        }
        return Transformations.map(coins, ArrayList::new);
    }

    private void fetchFromNetworkIfNecessary(Query query) {
        executor.submit(() -> {
            if (query.forceUpdate() || db.coins().coinsCount() == 0) {
                try {
                    final Response<Listings> response = api.listings(query.currency()).execute();
                    if (response.isSuccessful()) {
                        final Listings listings = response.body();
                        if (listings != null) {
                            saveCoinsIntoDb(query, listings.data());
                        }
                    } else {
                        final ResponseBody responseBody = response.errorBody();
                        if (responseBody != null) {
                            throw new IOException(responseBody.string());
                        }
                    }
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        });
    }

    private void saveCoinsIntoDb(Query query, List<? extends Coin> coins) {
        List<RoomCoin> roomCoins = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            roomCoins.add(RoomCoin.create(
                coin.name(),
                coin.symbol(),
                coin.rank(),
                coin.price(),
                coin.change24h(),
                query.currency(),
                coin.id()
            ));
        }
        db.coins().insert(roomCoins);
    }

}
