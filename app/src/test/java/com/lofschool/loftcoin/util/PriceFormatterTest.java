package com.lofschool.loftcoin.util;

import android.content.Context;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.NumberFormat;
import java.util.Locale;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class PriceFormatterTest {

    private Context context;

    private PriceFormatter formatter;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        formatter = new PriceFormatter(context);
    }

    @Test
    public void format_EUR() {
        assertThat(formatter
            .format("EUR", 1.23)).isEqualTo(NumberFormat
            .getCurrencyInstance(Locale.GERMANY).format(1.23));
    }

    @Test
    public void format_RUB() {
        assertThat(formatter
            .format("RUB", 1.23)).isEqualTo(NumberFormat
            .getCurrencyInstance(new Locale("ru", "RU")).format(1.23));
    }

    @Test
    public void format_default() {
        final LocaleListCompat locales = ConfigurationCompat
            .getLocales(context.getResources().getConfiguration());

        assertThat(formatter
            .format("CAD", 1.23)).isEqualTo(NumberFormat
            .getCurrencyInstance(locales.get(0)).format(1.23));
    }

}