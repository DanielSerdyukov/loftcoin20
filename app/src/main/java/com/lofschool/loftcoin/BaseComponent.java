package com.lofschool.loftcoin;

import android.content.Context;

import com.lofschool.loftcoin.data.CoinsRepo;
import com.lofschool.loftcoin.data.CurrencyRepo;

public interface BaseComponent {
    Context context();
    CoinsRepo coinsRepo();
    CurrencyRepo currencyRepo();
}
