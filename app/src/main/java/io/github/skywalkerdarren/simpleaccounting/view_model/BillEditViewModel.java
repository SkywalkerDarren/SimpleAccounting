package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
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

    /**
     * @param account 设置的账户
     */
    public void setAccount(Account account) {
        mAccount = account;
        notifyChange();
    }

    /**
     * @param type 设置类型
     */
    public void setType(Type type) {
        mType = type;
        notifyChange();
    }

    /**
     * @return 类型名
     */
    @Bindable
    public String getTypeName() {
        return mType.getName();
    }

    /**
     * @return 类型图id
     */
    @Bindable
    public int getTypeImg() {
        return mType.getTypeId();
    }

    /**
     * @return 类型id
     */
    public UUID getTypeId() {
        return mType.getId();
    }

    /**
     * @return 账户id
     */
    public UUID getAccountId() {
        return mAccount.getId();
    }

    /**
     * @return 账户图id
     */
    @Bindable
    public int getAccountImg() {
        return mAccount.getImageId();
    }

    /**
     * @return 账户背景色值
     */
    @Bindable
    public int getAccountColor() {
        return mAccount.getColor();
    }

    /**
     * @return 账单收支
     */
    @Bindable
    public String getBalance() {
        return mBill.getBalance().toString();
    }

    /**
     * @return 账单日期
     */
    @Bindable
    public DateTime getDate() {
        return mBill.getDate();
    }

    /**
     * @param date 设置账单日期
     */
    public void setDate(DateTime date) {
        mBill.setDate(date);
    }

    /**
     * @return 账单备注
     */
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
            r = mType.getExpense() ? r.negate() : r;
            mAccount.plusBalance(r);
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

    /**
     * @return true为支出类型
     */
    public boolean getExpense() {
        return mType.getExpense();
    }
}
