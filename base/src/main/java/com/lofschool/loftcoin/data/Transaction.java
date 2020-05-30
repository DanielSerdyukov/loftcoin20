package com.lofschool.loftcoin.data;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Transaction {

    public static Transaction create(String uid,
                                     Coin coin,
                                     double amount,
                                     Date createdAt) {
        return new AutoValue_Transaction(uid, coin, amount, createdAt);
    }

    public abstract String uid();

    public abstract Coin coin();

    public abstract double amount();

    public abstract Date createdAt();

}
