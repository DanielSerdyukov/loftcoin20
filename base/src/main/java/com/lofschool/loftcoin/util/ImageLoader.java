package com.lofschool.loftcoin.util;

import android.widget.ImageView;

import androidx.annotation.NonNull;

public interface ImageLoader {

    @NonNull
    ImageRequest load(String url);

    interface ImageRequest {
        void into(@NonNull ImageView view);
    }

}
