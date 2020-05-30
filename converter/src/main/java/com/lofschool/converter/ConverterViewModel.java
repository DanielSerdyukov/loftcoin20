package com.lofschool.converter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.lofschool.loftcoin.data.Coin;
import com.lofschool.loftcoin.data.CoinsRepo;
import com.lofschool.loftcoin.data.CurrencyRepo;
import com.lofschool.loftcoin.util.RxSchedulers;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class ConverterViewModel extends ViewModel {

    private final Subject<Coin> fromCoin = BehaviorSubject.create();

    private final Subject<Coin> toCoin = BehaviorSubject.create();

    private final Subject<String> fromValue = BehaviorSubject.create();

    private final Subject<String> toValue = BehaviorSubject.create();

    private final Observable<List<Coin>> topCoins;

    private final Observable<Double> factor;

    private final RxSchedulers schedulers;

    @Inject
    ConverterViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {
        this.schedulers = schedulers;

        topCoins = currencyRepo.currency()
            .switchMap(coinsRepo::topCoins)
            .doOnNext((coins) -> fromCoin.onNext(coins.get(0)))
            .doOnNext((coins) -> toCoin.onNext(coins.get(1)))
            .replay(1)
            .autoConnect();

        factor = fromCoin
            .flatMap((fc) -> toCoin
                .map((tc) -> fc.price() / tc.price())
            )
            .replay(1)
            .autoConnect();
    }

    @NonNull
    Observable<List<Coin>> topCoins() {
        return topCoins.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Coin> fromCoin() {
        return fromCoin.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Coin> toCoin() {
        return toCoin.observeOn(schedulers.main());
    }

    @NonNull
    Observable<String> fromValue() {
        return fromValue.observeOn(schedulers.main());
    }

    @NonNull
    Observable<String> toValue() {
        return fromValue
            .observeOn(schedulers.cmp())
            .map((s) -> s.isEmpty() ? "0.0" : s)
            .map(Double::parseDouble)
            .flatMap((value) -> factor.map((f) -> value * f))
            .map(v -> String.format(Locale.US, "%.2f", v))
            .map((v) -> "0.0".equals(v) ? "" : v)
            .observeOn(schedulers.main());
    }

    void fromCoin(Coin coin) {
        fromCoin.onNext(coin);
    }

    void toCoin(Coin coin) {
        toCoin.onNext(coin);
    }

    void fromValue(CharSequence text) {
        fromValue.onNext(text.toString());
    }

    void toValue(CharSequence text) {
        toValue.onNext(text.toString());
    }

}
