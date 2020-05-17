package com.lofschool.loftcoin.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.lofschool.loftcoin.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;

@Singleton
class NotifierImpl implements Notifier {

    private final Context context;

    private final RxSchedulers schedulers;

    private final NotificationManager ntf;

    @Inject
    NotifierImpl(Context context, RxSchedulers schedulers) {
        this.context = context;
        this.schedulers = schedulers;
        this.ntf = (NotificationManager) context
            .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Completable sendMessage(@NonNull String title, @NonNull String message, @NonNull Class<?> receiver) {
        return Completable
            .fromAction(() -> {
                final String channelId = context.getString(R.string.default_channel_id);
                final Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(
                        context,
                        0,
                        new Intent(context, receiver)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        PendingIntent.FLAG_ONE_SHOT,
                        Bundle.EMPTY
                    ))
                    .build();
                ntf.notify(1, notification);
            })
            .startWith(createDefaultChannel())
            .subscribeOn(schedulers.main());
    }

    private Completable createDefaultChannel() {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ntf.createNotificationChannel(new NotificationChannel(
                    context.getString(R.string.default_channel_id),
                    context.getString(R.string.default_channel_name),
                    NotificationManager.IMPORTANCE_LOW
                ));
            }
        });
    }

}
