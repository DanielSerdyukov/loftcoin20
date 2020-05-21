package com.lofschool.loftcoin.ui.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.lofschool.loftcoin.data.Coin;
import com.lofschool.loftcoin.data.CurrencyRepo;
import com.lofschool.loftcoin.data.Transaction;
import com.lofschool.loftcoin.data.Wallet;
import com.lofschool.loftcoin.data.WalletsRepo;
import com.lofschool.loftcoin.util.RxSchedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

class WalletsViewModel extends ViewModel {

    private final Subject<Integer> walletPosition = BehaviorSubject.createDefault(0);

    private final Observable<List<Wallet>> wallets;

    private final Observable<List<Transaction>> transactions;

    private final WalletsRepo walletsRepo;

    private final CurrencyRepo currencyRepo;

    private final RxSchedulers schedulers;

    @Inject
    WalletsViewModel(WalletsRepo walletsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {
        this.walletsRepo = walletsRepo;
        this.currencyRepo = currencyRepo;
        this.schedulers = schedulers;

        wallets = currencyRepo.currency()
            .switchMap(walletsRepo::wallets)
            .replay(1)
            .autoConnect();

        transactions = wallets
            .filter((w) -> !w.isEmpty())
            .switchMap((wallets) -> walletPosition
                .map((pos) -> Math.min(pos, wallets.size() - 1))
                .map(wallets::get)
            )
            .switchMap(walletsRepo::transactions)
            .replay(1)
            .autoConnect();
    }

    @NonNull
    Observable<List<Wallet>> wallets() {
        return wallets.observeOn(schedulers.main());
    }

    @NonNull
    Observable<List<Transaction>> transactions() {
        return transactions.observeOn(schedulers.main());
    }

    @NonNull
    Completable addWallet() {
        return wallets
            .firstOrError()
            .flatMap((list) -> Observable
                .fromIterable(list)
                .map(Wallet::coin)
                .map(Coin::id)
                .toList()
                .doOnSuccess(u -> Timber.d("%s", u))
            )
            .flatMapCompletable((ids) -> currencyRepo
                .currency()
                .doOnNext(u -> Timber.d("%s", u))
                .flatMapCompletable((c) -> walletsRepo.addWallet(c, ids))
            )
            .observeOn(schedulers.main());
    }

    void changeWallet(int position) {
        walletPosition.onNext(position);
    }

}
