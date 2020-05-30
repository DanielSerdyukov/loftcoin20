package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Wallet {

    @NonNull
    static Wallet create(String id, Coin coin, Double balance) {
        return new AutoValue_Wallet(id, coin, balance);
    }

    public abstract String uid();

    public abstract Coin coin();

    public abstract double balance();

}
