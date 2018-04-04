package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.Type;
import io.github.skywalkerdarren.simpleaccounting.model.TypeLab;
import io.github.skywalkerdarren.simpleaccounting.ui.BillEditActivity;

/**
 * @author darren
 * @date 2018/4/4
 */

public class BillDetailViewModel extends BaseObservable {
    private Bill mBill;
    private Type mType;
    private Account mAccount;
    private Activity mActivity;

    public BillDetailViewModel(Bill bill, Activity activity) {
        mActivity = activity;
        mBill = bill;
        mAccount = AccountLab.getInstance(mActivity).getAccount(mBill.getAccountId());
        mType = TypeLab.getInstance(mActivity).getType(mBill.getTypeId());
    }

    @BindingAdapter("android:src")
    public static void setTypeImage(ImageView view, int res) {
        view.setImageResource(res);
    }

    @BindingAdapter("android:textColor")
    public static void setBalanceColor(TextView tv, int color) {
        tv.setTextColor(color);
    }

    @DrawableRes
    public int getTypeImage() {
        return mType.getTypeId();
    }

    public String getTypeName() {
        return mType.getName();
    }

    public String getBalance() {
        return mBill.getBalance().toString();
    }

    public int getBalanceColor() {
        return mActivity.getResources().getColor(mType.getExpense() ?
                R.color.deeporange800 :
                R.color.lightgreen700);
    }

    public String getAccountName() {
        return mAccount.getName();
    }

    public String getRecorder() {
        // TODO: 2018/4/4 记录人空缺
        return "暂无";
    }

    public String getTime() {
        return mBill.getDate().toString("yyyy-MM-dd hh:mm");
    }

    public String getRemark() {
        return mBill.getRemark();
    }

    public void onEditFabClick(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = (int) view.getX() + view.getWidth() / 2;
        int y = (int) view.getY() + view.getHeight() / 2;
        Intent intent = BillEditActivity.newIntent(mActivity, mBill, x, y);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity);
        mActivity.startActivity(intent, options.toBundle());
    }
}
