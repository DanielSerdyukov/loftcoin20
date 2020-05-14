package com.lofschool.loftcoin.ui.rates;

import androidx.lifecycle.ViewModelProvider;

import com.lofschool.loftcoin.BaseComponent;
import com.lofschool.loftcoin.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    RatesModule.class,
    ViewModelModule.class
}, dependencies = {
    BaseComponent.class
})
abstract class RatesComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract RatesAdapter ratesAdapter();

}
