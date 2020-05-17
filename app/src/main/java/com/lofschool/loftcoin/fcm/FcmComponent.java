package com.lofschool.loftcoin.fcm;

import com.lofschool.loftcoin.BaseComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    FcmModule.class
}, dependencies = {
    BaseComponent.class
})
abstract class FcmComponent {

    abstract void inject(FcmService service);

}
