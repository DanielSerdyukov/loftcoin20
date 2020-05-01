package com.lofschool.loftcoin.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Coin {

    public abstract int id();

    public abstract String name();

    public abstract String symbol();

}
