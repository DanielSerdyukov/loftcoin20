package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Singleton
class WalletsRepoImpl implements WalletsRepo {

    private final Random random = new SecureRandom();

    private final FirebaseFirestore firestore;

    private final CoinsRepo coinsRepo;

    @Inject
    WalletsRepoImpl(CoinsRepo coinsRepo) {
        this.firestore = FirebaseFirestore.getInstance();
        this.coinsRepo = coinsRepo;
    }

    @NonNull
    @Override
    public Observable<List<Wallet>> wallets(@NonNull Currency currency) {
        return Observable
            .<QuerySnapshot>create(emitter -> {
                final ListenerRegistration registration = firestore
                    .collection("wallets")
                    .orderBy("created_at", Query.Direction.ASCENDING)
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
            .concatMapSingle((documents) -> Observable
                .fromIterable(documents)
                .concatMapSingle((document) -> coinsRepo
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
        return Observable
            .<QuerySnapshot>create(emitter -> {
                final ListenerRegistration registration = firestore
                    .collection("wallets")
                    .document(wallet.uid())
                    .collection("transactions")
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
                .map((document) -> Transaction.create(
                    document.getId(),
                    wallet.coin(),
                    document.getDouble("amount"),
                    document.getDate("created_at")
                ))
                .toList()
            );
    }

    @NonNull
    @Override
    public Completable addWallet(@NonNull Currency currency, List<Integer> takenIds) {
        return coinsRepo.nextPopularCoin(currency, takenIds)
            .map((coin) -> {
                Map<String, Object> data = new HashMap<>();
                data.put("balance", 100 * (random.nextDouble() + 0.01));
                data.put("coinId", coin.id());
                data.put("created_at", FieldValue.serverTimestamp());
                return data;
            })
            .flatMapCompletable((wallet) -> Completable.create((emitter) -> {
                firestore.collection("wallets").add(wallet)
                    .addOnSuccessListener((r) -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(emitter::tryOnError);
            }));
    }

}
