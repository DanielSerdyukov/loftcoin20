package com.lofschool.loftcoin.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PriceFormatter implements Formatter<Double> {

    private static final Map<String, Locale> LOCALES = new HashMap<>();

    static {
        LOCALES.put("RUB", new Locale("ru", "RU"));
        LOCALES.put("EUR", Locale.GERMANY);
    }

    private final Context context;

    @Inject
    PriceFormatter(Context context) {
        this.context = context;
    }

    @NonNull
    public String format(@NonNull String currency, @NonNull Double value) {
        Locale locale = LOCALES.get(currency);
        if (locale == null) {
            final LocaleListCompat locales = ConfigurationCompat.getLocales(
                context.getResources().getConfiguration());
            locale = locales.get(0);
        }
        return NumberFormat.getCurrencyInstance(locale).format(value);
    }

    @NonNull
    @Override
    public String format(@NonNull Double value) {
        return NumberFormat.getCurrencyInstance().format(value);
    }

}
