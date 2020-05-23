package com.lofschool.loftcoin.util;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TestRxSchedulers implements RxSchedulers {
    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler cmp() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler main() {
        return Schedulers.trampoline();
    }
}
