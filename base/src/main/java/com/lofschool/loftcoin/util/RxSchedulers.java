package com.lofschool.loftcoin.util;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;

public interface RxSchedulers {
    @NonNull
    Scheduler io();

    @NonNull
    Scheduler cmp();

    @NonNull
    Scheduler main();
}
