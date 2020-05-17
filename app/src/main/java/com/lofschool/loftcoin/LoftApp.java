package com.lofschool.loftcoin;

import android.app.Application;
import android.os.StrictMode;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lofschool.loftcoin.util.DebugTree;

import timber.log.Timber;

public class LoftApp extends Application {

    private BaseComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree());
        }
        component = DaggerAppComponent.builder()
            .application(this)
            .build();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            Timber.d("fcm: %s", instanceIdResult.getToken());
        });
    }

    public BaseComponent getComponent() {
        return component;
    }

}
