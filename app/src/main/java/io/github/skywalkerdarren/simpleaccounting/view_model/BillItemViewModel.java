package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.ui.BillDetailActivity;

/**
 * @author darren
 * @date 2018/4/5
 */

public class BillItemViewModel extends BaseObservable {

    private static int mX;
    private static int mY;
    private BillInfo mBillInfo;
    private Activity mActivity;
    private ImageView mImage;

    public BillItemViewModel(BillInfo billinfo, Activity activity) {
        mActivity = activity;
        mBillInfo = billinfo;
    }

    @BindingAdapter("src")
    public static void setTypeImg(ImageView view, int res) {
        view.setImageResource(res);

    }

    public void setImagePair(ImageView view) {
        mImage = view;
    }

    @BindingAdapter("android:textColor")
    public static void setBalanceColor(TextView view, int color) {
        view.setTextColor(color);
    }

    public int getTypeRes() {
        return mBillInfo.getBillTypeResId();
    }

    public String getTitle() {
        return mBillInfo.getTitle();
    }

    public String getBalance() {
        return mBillInfo.getBalance();
    }

    public String getRemark() {
        return mBillInfo.getRemark();
    }

    public int getBalanceColor() {
        return mBillInfo.isExpense() ?
                Color.rgb(0xff, 0x70, 0x43) :
                Color.rgb(0x7C, 0xB3, 0x42);
    }

    public String getDate() {
        return mBillInfo.getDateTime().toString("yyyy-MM-dd");
    }

    public String getIncome() {
        return mBillInfo.getIncome();
    }

    public String getExpense() {
        return mBillInfo.getExpense();
    }

    public void click() {
        Intent intent = BillDetailActivity.newIntent(mActivity,
                BillLab.getInstance(mActivity).getBill(mBillInfo.getUUID()),
                mX, mY, R.color.orangea200);
        intent.putExtra(BillDetailActivity.EXTRA_START_COLOR, R.color.orangea200);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                mActivity, mImage, "type_image_view");
        mActivity.startActivity(intent, options.toBundle());
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("location")
    public static void setLocation(View view, boolean b) {
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
