package com.lofschool.loftcoin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CircleIndicator extends RecyclerView.ItemDecoration {

    private final Paint inactivePaint = new Paint();

    private final Paint activePaint = new Paint();

    private final float indicatorRadius;

    public CircleIndicator(@NonNull Context context) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        indicatorRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm);

        inactivePaint.setStyle(Paint.Style.FILL);
        inactivePaint.setColor(0x44ffffff); // White with alpha
        inactivePaint.setAntiAlias(true);

        activePaint.setStyle(Paint.Style.FILL);
        activePaint.setColor(Color.WHITE);
        activePaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter != null) {
            float totalWidth = 2 * indicatorRadius * adapter.getItemCount();
            float posX = (parent.getWidth() - totalWidth) / 2f;
            float posY = parent.getHeight() - 2 * indicatorRadius;
            final RecyclerView.LayoutManager lm = parent.getLayoutManager();
            int currentIndicator = RecyclerView.NO_POSITION;
            if (lm instanceof LinearLayoutManager) {
                currentIndicator = ((LinearLayoutManager) lm).findFirstCompletelyVisibleItemPosition();
            }
            for (int i = 0; i < adapter.getItemCount(); ++i) {
                drawIndicator(c, posX + 4 * indicatorRadius * i, posY, currentIndicator == i);
            }
        }
    }

    private void drawIndicator(@NonNull Canvas c, float x, float y, boolean active) {
        c.drawCircle(x, y, indicatorRadius, active ? activePaint : inactivePaint);
    }

}
