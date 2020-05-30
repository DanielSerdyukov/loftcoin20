package com.lofschool.loftcoin.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import io.reactivex.Observable;
import io.reactivex.android.MainThreadDisposable;

public class RecyclerViews {

    @NonNull
    public static Observable<Integer> onSnap(@NonNull RecyclerView rv, @NonNull SnapHelper helper) {
        return Observable.create((emitter) -> {
            MainThreadDisposable.verifyMainThread();
            final RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                        final View snapView = helper.findSnapView(rv.getLayoutManager());
                        if (snapView != null) {
                            final RecyclerView.ViewHolder holder = rv.findContainingViewHolder(snapView);
                            if (holder != null) {
                                emitter.onNext(holder.getAdapterPosition());
                            }
                        }
                    }
                }
            };
            emitter.setCancellable(() -> rv.removeOnScrollListener(listener));
            rv.addOnScrollListener(listener);
        });
    }

    @NonNull
    public static Observable<Integer> onClick(@NonNull RecyclerView rv) {
        return Observable.create((emitter) -> {
            MainThreadDisposable.verifyMainThread();
            final RecyclerView.OnItemTouchListener listener = new OnItemClick((v) -> {
                final RecyclerView.ViewHolder holder = rv.findContainingViewHolder(v);
                if (holder != null) {
                    emitter.onNext(holder.getAdapterPosition());
                }
            });
            emitter.setCancellable(() -> rv.removeOnItemTouchListener(listener));
            rv.addOnItemTouchListener(listener);
        });
    }

}
