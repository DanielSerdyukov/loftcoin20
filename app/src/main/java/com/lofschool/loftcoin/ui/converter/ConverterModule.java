package com.lofschool.loftcoin.ui.converter;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
abstract class ConverterModule {

    @Binds
    @IntoMap
    @ClassKey(ConverterViewModel.class)
    abstract ViewModel converterViewModel(ConverterViewModel impl);

}
