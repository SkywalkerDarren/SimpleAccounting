package io.github.skywalkerdarren.simpleaccounting.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by darren on 2018/1/29.
 * 账单类
 * 记录账单详细信息
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


    public Bill(UUID id) {
        mId = id;
    }

    public Bill() {
        this(UUID.randomUUID());
    }

    public UUID getId() {
        return mId;
    }

    public DateTime getDate() {
        return mDate;
    }

    public void setDate(DateTime date) {
        mDate = date;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setExpense(Type type) {
        isExpense = type.getExpense();
    }

    public void setExpense(boolean isExpense) {
        this.isExpense = isExpense;
    }


    public boolean isExpense() {
        return isExpense;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    public void setBalance(BigDecimal balance) {
        mBalance = balance;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

}
