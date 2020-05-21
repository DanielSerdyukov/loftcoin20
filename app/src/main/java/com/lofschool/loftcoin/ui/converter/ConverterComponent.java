package com.lofschool.loftcoin.ui.converter;

import androidx.lifecycle.ViewModelProvider;

import com.lofschool.loftcoin.BaseComponent;
import com.lofschool.loftcoin.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    ConverterModule.class,
    ViewModelModule.class
}, dependencies = {
    BaseComponent.class
})
abstract class ConverterComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract CoinsSheetAdapter coinsSheetAdapter();

}
