package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget;

import static io.github.skywalkerdarren.simpleaccounting.model.entity.Account.FOLDER;

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
    public String getTypeImg() {
        return Type.FOLDER + mType.getAssetsName();
    }

    /**
     * @return 类型id
     */
    public UUID getTypeId() {
        return mBill.getTypeId();
    }

    /**
     * @return 账户id
     */
    public UUID getAccountId() {
        return mBill.getAccountId();
    }

    /**
     * @return 账户图id
     */
    @Bindable
    public String getAccountImg() {
        return FOLDER + mAccount.getBitmap();
    }

    /**
     * @return 账户背景色值
     */
    @Bindable
    public int getAccountColor() {
        return mContext.getResources().getColor(mAccount.getColorId());
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
        } catch (Exception e) {
            Toast.makeText(mContext, "表达式错误", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 设定账单
        mBill.setName(getTypeName());
        mBill.setDate(getDate());
        mBill.setRemark(remark);
        mBill.setTypeId(mType.getUUID());
        mBill.setAccountId(mAccount.getUUID());

        // 刷新账单数据库
        BillLab billLab = BillLab.getInstance(mContext);
        if (billLab.getBill(mBill.getUUID()) == null) {
            billLab.addBill(mBill);
        } else {
            billLab.updateBill(mBill);
        }
        DesktopWidget.refresh(mContext);
        return true;
    }

    /**
     * @return true为支出类型
     */
    public boolean getExpense() {
        return mType.getIsExpense();
    }
}
