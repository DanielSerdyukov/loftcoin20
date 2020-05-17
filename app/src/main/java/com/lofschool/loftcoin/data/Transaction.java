package com.lofschool.loftcoin.data;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Transaction {

    public abstract String uid();

    public abstract Coin coin();

    public abstract double amount();

    public abstract Date createdAt();

}
