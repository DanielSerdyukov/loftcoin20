package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    public List<? extends Coin> listings(@NonNull String currency) throws IOException {
        final Response<Listings> response = api.listings(currency).execute();
        if (response.isSuccessful()) {
            final Listings listings = response.body();
            if (listings != null) {
                return listings.data();
            }
        } else {
            final ResponseBody responseBody = response.errorBody();
            if (responseBody != null) {
                throw new IOException(responseBody.string());
            }
        }
        return Collections.emptyList();
    }

    @NonNull
    @Override
    public LiveData<List<Coin>> listings(@NonNull Query query) {
        final MutableLiveData<Boolean> refresh = new MutableLiveData<>();
        executor.submit(() -> refresh.postValue(query.forceUpdate() || db.coins().coinsCount() == 0));
        // (t|f) ->
        return Transformations.switchMap(refresh, (r) -> {
            if (r) return fetchFromNetwork(query);
            else return fetchFromDb(query);
        });
    }

    private LiveData<List<Coin>> fetchFromDb(Query query) {
        return Transformations.map(db.coins().fetchAll(), ArrayList::new);
    }

    private LiveData<List<Coin>> fetchFromNetwork(Query query) {
        final MutableLiveData<List<Coin>> liveData = new MutableLiveData<>();
        executor.submit(() -> {
            try {
                final Response<Listings> response = api.listings(query.currency()).execute();
                if (response.isSuccessful()) {
                    final Listings listings = response.body();
                    if (listings != null) {
                        final List<AutoValue_CmcCoin> cmcCoins = listings.data();
                        saveCoinsIntoDb(cmcCoins);
                        liveData.postValue(new ArrayList<>(cmcCoins));
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
        });
        return liveData;
    }

    private void saveCoinsIntoDb(List<? extends Coin> coins) {
        List<RoomCoin> roomCoins = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            roomCoins.add(RoomCoin.create(
                coin.name(),
                coin.symbol(),
                coin.rank(),
                coin.price(),
                coin.change24h(),
                coin.id()
            ));
        }
        db.coins().insert(roomCoins);
    }

}
