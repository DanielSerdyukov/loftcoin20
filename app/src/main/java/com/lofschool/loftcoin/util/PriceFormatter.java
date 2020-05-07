package com.lofschool.loftcoin.util;

import android.os.Build;

import androidx.annotation.NonNull;

public class PriceFormatter implements Formatter<Double> {

    @NonNull
    @Override
    public String format(@NonNull Double value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return android.icu.text.NumberFormat.getCurrencyInstance().format(value);
        } else {
            return java.text.NumberFormat.getCurrencyInstance().format(value);
        }
    }

}
