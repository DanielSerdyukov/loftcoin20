package com.lofschool.loftcoin;

import android.app.Application;

import com.lofschool.loftcoin.data.DataModule;
import com.lofschool.loftcoin.util.UtilModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
    AppModule.class,
    DataModule.class,
    UtilModule.class
})
abstract class AppComponent implements BaseComponent {

    @Component.Builder
    static abstract class Builder {
        @BindsInstance
        abstract Builder application(Application app);

        abstract AppComponent build();
    }

}
