package com.lofschool.loftcoin;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
abstract class AppModule {

    @Singleton
    @Provides
    static Context context(Application app) {
        return app.getApplicationContext();
    }

}
