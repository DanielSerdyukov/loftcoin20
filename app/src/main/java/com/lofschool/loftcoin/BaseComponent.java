package com.lofschool.loftcoin;

import android.content.Context;

import com.lofschool.loftcoin.data.CoinsRepo;
import com.lofschool.loftcoin.data.CurrencyRepo;
import com.lofschool.loftcoin.util.ImageLoader;

public interface BaseComponent {
    Context context();
    CoinsRepo coinsRepo();
    CurrencyRepo currencyRepo();
    ImageLoader imageLoader();
}
