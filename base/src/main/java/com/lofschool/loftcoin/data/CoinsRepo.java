package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CoinsRepo {

    @NonNull
    Observable<List<Coin>> listings(@NonNull Query query);

    @NonNull
    Single<Coin> coin(@NonNull Currency currency, long id);

    @NonNull
    Single<Coin> nextPopularCoin(@NonNull Currency currency, List<Integer> ids);

    @NonNull
    Observable<List<Coin>> topCoins(@NonNull Currency currency);

    @AutoValue
    abstract class Query {

        @NonNull
        public static Builder builder() {
            return new AutoValue_CoinsRepo_Query.Builder()
                .forceUpdate(true)
                .sortBy(SortBy.RANK);
        }

        public abstract String currency();

        public abstract boolean forceUpdate();

        public abstract SortBy sortBy();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder currency(String currency);

            public abstract Builder forceUpdate(boolean forceUpdate);

            public abstract Builder sortBy(SortBy sortBy);

            public abstract Query build();
        }
    }

}
