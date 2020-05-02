package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;

import com.lofschool.loftcoin.BuildConfig;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class CmcCoinsRepo implements CoinsRepo {

    private final CmcApi api;

    public CmcCoinsRepo() {
        api = createRetrofit(createHttpClient()).create(CmcApi.class);
    }

    @NonNull
    @Override
    public List<? extends Coin> listings(@NonNull String currency) throws IOException {
        final Response<Listings> response = api.listings(currency).execute();
        if (response.isSuccessful()) {
            final Listings listings = response.body();
            if (listings != null) {
                return listings.data();
            }
        } else {
            final ResponseBody responseBody = response.errorBody();
            if (responseBody != null) {
                throw new IOException(responseBody.string());
            }
        }
        return Collections.emptyList();
    }

    private OkHttpClient createHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
            final Request request = chain.request();
            return chain.proceed(request.newBuilder()
                .addHeader(CmcApi.API_KEY, BuildConfig.API_KEY)
                .build());
        });
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.redactHeader(CmcApi.API_KEY);
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    private Retrofit createRetrofit(OkHttpClient httpClient) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(httpClient);
        builder.baseUrl(BuildConfig.API_ENDPOINT);
        final Moshi moshi = new Moshi.Builder().build();
        builder.addConverterFactory(MoshiConverterFactory.create(
            moshi.newBuilder()
                .add(Coin.class, moshi.adapter(AutoValue_Coin.class))
                .add(Listings.class, moshi.adapter(AutoValue_Listings.class))
                .build()
        ));
        return builder.build();
    }

}
