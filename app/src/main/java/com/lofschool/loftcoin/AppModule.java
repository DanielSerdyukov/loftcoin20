package com.lofschool.loftcoin;

import android.app.Application;
import android.content.Context;
import android.net.TrafficStats;

import com.lofschool.loftcoin.data.CmcApi;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
abstract class AppModule {

    @Singleton
    @Provides
    static Context context(Application app) {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    static ExecutorService ioExecutor() {
        int poolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        final AtomicInteger threadIds = new AtomicInteger();
        return Executors.newFixedThreadPool(poolSize, r -> {
            final Thread thread = new Thread(r);
            final int threadId = threadIds.incrementAndGet();
            TrafficStats.setThreadStatsTag(threadId);
            thread.setName("io-" + threadId);
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        });
    }

    @Provides
    @Singleton
    static OkHttpClient httpClient(ExecutorService executor) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dispatcher(new Dispatcher(executor));
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.redactHeader(CmcApi.API_KEY);
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    static Picasso picasso(Context context, OkHttpClient httpClient, ExecutorService executor) {
        return new Picasso.Builder(context)
            .downloader(new OkHttp3Downloader(httpClient))
            .executor(executor)
            .build();
    }

}
