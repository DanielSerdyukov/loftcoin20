package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Currency {

    @NonNull
    static Currency create(String symbol, String code, String name) {
        return new AutoValue_Currency(symbol, code, name);
    }

    public abstract String symbol();

    public abstract String code();

    public abstract String name();

}
