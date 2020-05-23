package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class TestCoinsRepo implements CoinsRepo {

    public final Subject<List<Coin>> listings = PublishSubject.create();

    public Query lastListingsQuery;

    @NonNull
    @Override
    public Observable<List<Coin>> listings(@NonNull Query query) {
        lastListingsQuery = query;
        return listings;
    }

    @NonNull
    @Override
    public Single<Coin> coin(@NonNull Currency currency, long id) {
        return Single.error(() -> new AssertionError("Stub!"));
    }

    @NonNull
    @Override
    public Single<Coin> nextPopularCoin(@NonNull Currency currency, List<Integer> ids) {
        return Single.error(() -> new AssertionError("Stub!"));
    }

    @NonNull
    @Override
    public Observable<List<Coin>> topCoins(@NonNull Currency currency) {
        return Observable.error(() -> new AssertionError("Stub!"));
    }

}
