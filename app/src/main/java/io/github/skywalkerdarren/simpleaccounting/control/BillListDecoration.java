package io.github.skywalkerdarren.simpleaccounting.control;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by darren on 2018/2/2.
 */

public class BillListDecoration extends RecyclerView.ItemDecoration {
    private ColorDrawable mDivider;

    public BillListDecoration() {
        super();
        mDivider = new ColorDrawable(Color.GRAY);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int itemCount = parent.getChildCount();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                return;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + 1;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int current = parent.getChildAdapterPosition(view);
        int lastPosition = state.getItemCount() - 1;
        if (current == -1) {
            return;
        }
        outRect.set(0, 0, 0, 1);
        if (current == lastPosition) {
            outRect.set(0, 0, 0, 0);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

    }
}
