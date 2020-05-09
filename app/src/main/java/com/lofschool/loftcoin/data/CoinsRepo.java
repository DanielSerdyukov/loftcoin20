package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.google.auto.value.AutoValue;

import java.io.IOException;
import java.util.List;

public interface CoinsRepo {

    @NonNull
    @WorkerThread
    List<? extends Coin> listings(@NonNull String currency) throws IOException;

    @NonNull
    LiveData<List<Coin>> listings(@NonNull Query query);

    @AutoValue
    abstract class Query {

        @NonNull
        public static Builder builder() {
            return new AutoValue_CoinsRepo_Query.Builder()
                .forceUpdate(true);
        }

        abstract String currency();

        abstract boolean forceUpdate();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder currency(String currency);

            public abstract Builder forceUpdate(boolean forceUpdate);

            public abstract Query build();
        }
    }

}
