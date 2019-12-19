package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity;

/**
 * 空列表vm
 *
 * @author darren
 * @date 2018/4/5
 */

public class EmptyListViewModel extends BaseObservable {
    private static int mX;
    private static int mY;
    private Context mContext;

    public EmptyListViewModel(Context context) {
        mContext = context;
    }

    /**
     * 获取位置
     */
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

    /**
     * 点击跳转新建账单
     */
    public void onClick() {
        Bill bill = new Bill();
        Intent intent = BillEditActivity.newIntent(mContext, bill,
                mX, mY);
        mContext.startActivity(intent);
    }
}
