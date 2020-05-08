package com.lofschool.loftcoin.util;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnItemClick implements RecyclerView.OnItemTouchListener {

    private final View.OnClickListener listener;

    private long downTime;

    public OnItemClick(@NonNull View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (isSingleTap(e)) {
            final View view = rv.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                listener.onClick(view);
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private boolean isSingleTap(MotionEvent e) {
        if (MotionEvent.ACTION_DOWN == e.getActionMasked()) {
            downTime = SystemClock.uptimeMillis();
        } else if (MotionEvent.ACTION_UP == e.getActionMasked()) {
            return e.getEventTime() - downTime <= ViewConfiguration.getTapTimeout();
        }
        return false;
    }

}
