package com.lofschool.loftcoin.ui.currency;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
abstract class CurrencyModule {

    @Binds
    @IntoMap
    @ClassKey(CurrencyViewModel.class)
    abstract ViewModel currencyViewModel(CurrencyViewModel impl);

}
