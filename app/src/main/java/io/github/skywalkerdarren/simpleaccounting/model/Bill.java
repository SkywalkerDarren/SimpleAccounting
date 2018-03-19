package io.github.skywalkerdarren.simpleaccounting.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 账单类
 * 记录账单详细信息
 *
 * @author darren
 * @date 2018/1/29
 */

public class Bill implements Serializable {
    /**
     * 账单ID号
     */
    private UUID mId;

    /**
     * 账单日期
     */
    private DateTime mDate;

    /**
     * 账单名称
     */
    private String mName;

    /**
     * 账单类型
     */
    private String mType;

    /**
     * 账单收支
     */
    private BigDecimal mBalance;

    /**
     * 账单备注
     */
    private String mRemark;

    /**
     * 是否是支出类型
     */
    private boolean isExpense = true;


    /**
     * 通过id新建账单
     *
     * @param id 账单id
     */
    public Bill(UUID id) {
        mId = id;
    }

    /**
     * 新建账单
     */
    public Bill() {
        this(UUID.randomUUID());
    }

    /**
     * @return 账单id
     */
    public UUID getId() {
        return mId;
    }

    /**
     * @return 账单日期
     */
    public DateTime getDate() {
        return mDate;
    }

    /**
     * @param date 账单日期
     */
    public void setDate(DateTime date) {
        mDate = date;
    }

    /**
     * @return 账单标题
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name 账单标题
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * @return 账单类型名
     */
    public String getTypeName() {
        return mType;
    }

    /**
     * @return 账单对应资源id
     */
    public @DrawableRes
    int getTypeResId() {
        if (getTypeName() == null) {
            return 0;
        }
        List<BaseType> types = isExpense() ? ExpenseType.getTypeList() : IncomeType.getTypeList();
        for (BaseType t : types) {
            if (t.getName().equals(getTypeName())) {
                return t.getTypeId();
            }
        }
        return 0;
    }

    /**
     * @param type 账单类型
     */
    public void setType(String type) {
        mType = type;
    }

    /**
     * 是否为支出
     *
     * @param type 类型
     */
    public void setExpense(BaseType type) {
        isExpense = type.getExpense();
    }

    /**
     * 是否为支出
     *
     * @param isExpense true为支出类型
     */
    public void setExpense(boolean isExpense) {
        this.isExpense = isExpense;
    }

    /**
     * 是否为支出
     *
     * @return true为支出类型
     */
    public boolean isExpense() {
        return isExpense;
    }

    /**
     * @return 账单数额
     */
    public BigDecimal getBalance() {
        return mBalance;
    }

    /**
     * @param balance 账单数额
     */
    public void setBalance(BigDecimal balance) {
        mBalance = balance;
    }

    /**
     * @return 账单备注
     */
    @Nullable
    public String getRemark() {
        return mRemark;
    }

    /**
     * @param remark 账单备注
     */
    public void setRemark(String remark) {
        mRemark = remark;
    }

}
