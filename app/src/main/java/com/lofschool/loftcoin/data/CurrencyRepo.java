package com.lofschool.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public interface CurrencyRepo {

    @NonNull
    LiveData<List<Currency>> availableCurrencies();

    @NonNull
    LiveData<Currency> currency();

    void updateCurrency(@NonNull Currency currency);

}
