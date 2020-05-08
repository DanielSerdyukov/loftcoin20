package com.lofschool.loftcoin.ui.currency;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lofschool.loftcoin.data.Currency;
import com.lofschool.loftcoin.data.CurrencyRepo;

import java.util.List;

import javax.inject.Inject;

class CurrencyViewModel extends ViewModel {

    private final CurrencyRepo currencyRepo;

    @Inject
    CurrencyViewModel(CurrencyRepo currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

    @NonNull
    LiveData<List<Currency>> allCurrencies() {
        return currencyRepo.availableCurrencies();
    }

    void updateCurrency(@NonNull Currency currency) {
        currencyRepo.updateCurrency(currency);
    }

}
