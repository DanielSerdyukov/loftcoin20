package com.lofschool.loftcoin;

import android.content.Context;

import com.lofschool.loftcoin.data.CoinsRepo;
import com.lofschool.loftcoin.data.CurrencyRepo;
import com.lofschool.loftcoin.data.WalletsRepo;
import com.lofschool.loftcoin.util.ImageLoader;
import com.lofschool.loftcoin.util.Notifier;
import com.lofschool.loftcoin.util.RxSchedulers;

public interface BaseComponent {
    Context context();

    CoinsRepo coinsRepo();

    CurrencyRepo currencyRepo();

    WalletsRepo walletsRepo();

    ImageLoader imageLoader();

    RxSchedulers schedulers();

    Notifier notifier();
}
