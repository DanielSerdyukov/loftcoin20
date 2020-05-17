package com.lofschool.loftcoin.ui.wallets;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
abstract class WalletsModule {

    @Binds
    @IntoMap
    @ClassKey(WalletsViewModel.class)
    abstract ViewModel walletsViewModel(WalletsViewModel impl);

}
