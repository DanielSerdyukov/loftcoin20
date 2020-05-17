package com.lofschool.loftcoin.data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
class WalletsRepoImpl implements WalletsRepo {

    private final FirebaseFirestore firestore;

    private final CoinsRepo coinsRepo;

    @Inject
    WalletsRepoImpl(CoinsRepo coinsRepo) {
        this.firestore = FirebaseFirestore.getInstance();
        this.coinsRepo = coinsRepo;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Observable<List<Wallet>> wallets(@NonNull Currency currency) {
        return Observable
            .<QuerySnapshot>create(emitter -> {
                final ListenerRegistration registration = firestore.collection("wallets")
                    .addSnapshotListener((snapshots, e) -> {
                        if (emitter.isDisposed()) return;
                        if (snapshots != null) {
                            emitter.onNext(snapshots);
                        } else if (e != null) {
                            emitter.tryOnError(e);
                        }
                    });
                emitter.setCancellable(registration::remove);
            })
            .map(QuerySnapshot::getDocuments)
            .switchMapSingle((documents) -> Observable
                .fromIterable(documents)
                .switchMapSingle((document) -> coinsRepo
                    .coin(currency, Objects.requireNonNull(document
                        .getLong("coinId"), "coinId"))
                    .map((coin) -> Wallet.create(
                        document.getId(),
                        coin,
                        document.getDouble("balance")
                    ))
                )
                .toList()
            );
    }

    @NonNull
    @Override
    public Observable<List<Transaction>> transactions(@NonNull Wallet wallet) {
        return Observable.empty();
    }

}
