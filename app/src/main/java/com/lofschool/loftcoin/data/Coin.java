package com.lofschool.loftcoin.data;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.squareup.moshi.Json;

import java.util.Iterator;
import java.util.Map;

@AutoValue
public abstract class Coin {

    public abstract int id();

    public abstract String name();

    public abstract String symbol();

    @Memoized
    public double price() {
        final Iterator<? extends Quote> iterator = quote().values().iterator();
        if (iterator.hasNext()) return iterator.next().price();
        return 0d;
    }

    @Memoized
    public double change24h() {
        final Iterator<? extends Quote> iterator = quote().values().iterator();
        if (iterator.hasNext()) return iterator.next().change24h();
        return 0d;
    }

    @Json(name = "cmc_rank")
    @AutoValue.CopyAnnotations
    public abstract int rank();

    abstract Map<String, AutoValue_Coin_Quote> quote();

    @AutoValue
    abstract static class Quote {

        public abstract double price();

        @Json(name = "percent_change_24h")
        @AutoValue.CopyAnnotations
        public abstract double change24h();

    }

}
