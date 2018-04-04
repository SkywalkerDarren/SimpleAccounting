package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.model.Type;

/**
 * @author darren
 * @date 2018/4/4
 */

public class BillEditViewModel extends BaseObservable {
    private Context mContext;
    private Bill mBill;
    private Type mType;
    private Account mAccount;

    public BillEditViewModel(Bill bill, Context context) {
        mBill = bill;
        mContext = context;
    }

    public void setAccount(Account account) {
        mAccount = account;
        notifyChange();
    }

    public void setType(Type type) {
        mType = type;
        notifyChange();
    }

    @BindingAdapter("android:src")
    public static void setImg(ImageView view, int res) {
        view.setImageResource(res);
    }

    @BindingAdapter("app:cardBackgroundColor")
    public static void setColor(CardView view, int res) {
        view.setCardBackgroundColor(res);
    }

    @Bindable
    public String getTypeName() {
        return mType.getName();
    }

    @Bindable
    public int getTypeImg() {
        return mType.getTypeId();
    }

    public UUID getTypeId() {
        return mType.getId();
    }

    public UUID getAccountId() {
        return mAccount.getId();
    }

    @Bindable
    public int getAccountImg() {
        return mAccount.getImageId();
    }

    @Bindable
    public int getAccountColor() {
        return mAccount.getColor();
    }

    @Bindable
    public String getBalance() {
        return mBill.getBalance().toString();
    }

    @Bindable
    public DateTime getDate() {
        return mBill.getDate();
    }

    public void setDate(DateTime date) {
        mBill.setDate(date);
    }

    public String getRemark() {
        return mBill.getRemark();
    }

    /**
     * 保存账单
     */
    public boolean saveBill(String balance, String remark) {
        if (TextUtils.isEmpty(balance)) {
            return false;
        }
        try {
            BigDecimal r = new BigDecimal(balance);
            mBill.setBalance(r);
            // 设定账户
            if (mType.getExpense()) {
                mAccount.minusBalance(r);
            } else {
                mAccount.plusBalance(r);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "表达式错误", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 设定账单
        mBill.setName(getTypeName());
        mBill.setDate(getDate());
        mBill.setRemark(remark);
        mBill.setTypeId(mType.getId());
        mBill.setAccountId(mAccount.getId());

        // 刷新账单数据库
        BillLab billLab = BillLab.getInstance(mContext);
        if (billLab.getBill(mBill.getId()) == null) {
            billLab.addBill(mBill);
        } else {
            billLab.updateBill(mBill);
        }
        // 刷新账户数据库
        AccountLab accountLab = AccountLab.getInstance(mContext);
        accountLab.updateAccount(mAccount);
        return true;
    }

    public boolean getExpense() {
        return mType.getExpense();
    }
}
