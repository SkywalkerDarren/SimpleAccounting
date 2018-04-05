package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.MotionEvent;
import android.view.View;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.ui.BillEditActivity;

/**
 * @author darren
 * @date 2018/4/5
 */

public class EmptyListViewModel extends BaseObservable {
    private Context mContext;
    private static int mX;
    private static int mY;

    public EmptyListViewModel(Context context) {
        mContext = context;
    }

    public void onClick() {
        Bill bill = new Bill();
        Intent intent = BillEditActivity.newIntent(mContext, bill,
                mX, mY);
        mContext.startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("onTouch")
    public static void setTouchListener(View view, boolean b) {
        view.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mX = (int) motionEvent.getRawX();
                    mY = (int) motionEvent.getRawY();
                    break;
                default:
                    break;
            }
            return b;
        });
    }
}
