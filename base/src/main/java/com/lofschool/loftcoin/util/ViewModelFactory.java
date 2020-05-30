package com.lofschool.loftcoin.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Map<Class<?>, Provider<ViewModel>> providers;

    @Inject
    ViewModelFactory(Map<Class<?>, Provider<ViewModel>> providers) {
        this.providers = providers;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        final Provider<ViewModel> provider = providers.get(modelClass);
        if (provider != null) {
            return (T) provider.get();
        }
        return super.create(modelClass);
    }

}
