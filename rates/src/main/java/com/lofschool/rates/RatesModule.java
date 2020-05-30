package com.lofschool.rates;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
abstract class RatesModule {

    @Binds
    @IntoMap
    @ClassKey(RatesViewModel.class)
    abstract ViewModel ratesViewModel(RatesViewModel impl);

}
