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

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.ui.BillDetailActivity;

/**
 * 账单列表物品vm
 *
 * @author darren
 * @date 2018/4/5
 */

public class BillInfoViewModel extends BaseObservable {

    private static int mX;
    private static int mY;
    private BillInfo mBillInfo;
    private Activity mActivity;
    private ImageView mImage;

    public BillInfoViewModel(BillInfo billinfo, Activity activity) {
        mActivity = activity;
        mBillInfo = billinfo;
    }

    /**
     * 设置点击位置
     */
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

    /**
     * 设置共享图片
     */
    public void setImagePair(ImageView view) {
        mImage = view;
    }

    /**
     * @return 账单类型图id
     */
    public int getTypeRes() {
        return mBillInfo.getBillTypeResId();
    }

    /**
     * @return 账单标题
     */
    public String getTitle() {
        return mBillInfo.getTitle();
    }

    /**
     * @return 账单盈余
     */
    public String getBalance() {
        return mBillInfo.getBalance();
    }

    /**
     * @return 账单备注
     */
    public String getRemark() {
        return mBillInfo.getRemark();
    }

    /**
     * @return 收支颜色
     */
    public int getBalanceColor() {
        return mBillInfo.isExpense() ?
                Color.rgb(0xff, 0x70, 0x43) :
                Color.rgb(0x7C, 0xB3, 0x42);
    }

    /**
     * @return 账单日期
     */
    public String getDate() {
        return mBillInfo.getDateTime().toString("yyyy-MM-dd");
    }

    /**
     * @return 账单收入
     */
    public String getIncome() {
        return mBillInfo.getIncome();
    }

    /**
     * @return 账单支出
     */
    public String getExpense() {
        return mBillInfo.getExpense();
    }

    /**
     * 点击跳转
     */
    public void click() {
        Intent intent = BillDetailActivity.newIntent(mActivity,
                BillLab.getInstance(mActivity).getBill(mBillInfo.getUUID()),
                mX, mY, R.color.orangea200);
        intent.putExtra(BillDetailActivity.EXTRA_START_COLOR, R.color.orangea200);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                mActivity, mImage, "type_image_view");
        mActivity.startActivity(intent, options.toBundle());
    }
}
