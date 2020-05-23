package com.lofschool.loftcoin.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.lofschool.loftcoin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
class CurrencyRepoImpl implements CurrencyRepo {

    private static final String KEY_CURRENCY = "currency";

    private final Map<String, Currency> availableCurrencies = new HashMap<>();

    private SharedPreferences prefs;

    @Inject
    CurrencyRepoImpl(@NonNull Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        availableCurrencies.put("USD", Currency.create("$", "USD", context.getString(R.string.usd)));
        availableCurrencies.put("EUR", Currency.create("E", "EUR", context.getString(R.string.eur)));
        availableCurrencies.put("RUB", Currency.create("R", "RUB", context.getString(R.string.rub)));
    }

    @NonNull
    @Override
    public LiveData<List<Currency>> availableCurrencies() {
        final MutableLiveData<List<Currency>> liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<>(availableCurrencies.values()));
        return liveData;
    }

    @NonNull
    @Override
    public Observable<Currency> currency() {
        return Observable.create(emitter -> {
            SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> {
                if (!emitter.isDisposed() && KEY_CURRENCY.equals(key)) {
                    emitter.onNext(availableCurrencies.get(prefs.getString(key, "USD")));
                }
            };
            prefs.registerOnSharedPreferenceChangeListener(listener);
            emitter.setCancellable(() -> prefs.unregisterOnSharedPreferenceChangeListener(listener));
            emitter.onNext(availableCurrencies.get(prefs.getString(KEY_CURRENCY, "USD")));
        });
    }

    @Override
    public void updateCurrency(@NonNull Currency currency) {
        prefs.edit().putString(KEY_CURRENCY, currency.code()).apply();
    }

}
