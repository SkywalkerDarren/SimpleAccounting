package io.github.skywalkerdarren.simpleaccounting.model;

import androidx.annotation.Nullable;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
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
     * 账单类型ID
     */
    private UUID mTypeId;

    /**
     * 账单账户类型ID
     */
    private UUID mAccountId;

    /**
     * 账单日期
     */
    private DateTime mDate;

    /**
     * 账单名称
     */
    private String mName;

    /**
     * 账单收支
     */
    private BigDecimal mBalance;

    /**
     * 账单备注
     */
    private String mRemark;

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
    public Bill setDate(DateTime date) {
        mDate = date;
        return this;
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
    public Bill setName(String name) {
        mName = name;
        return this;
    }

    /**
     * @return 账单类型名
     */
    public UUID getTypeId() {
        return mTypeId;
    }

    /**
     * @param typeId 类型ID
     */
    public Bill setTypeId(UUID typeId) {
        mTypeId = typeId;
        return this;
    }

    /**
     * @return 账户ID
     */
    public UUID getAccountId() {
        return mAccountId;
    }

    /**
     * @param accountId 账户ID
     */
    public Bill setAccountId(UUID accountId) {
        mAccountId = accountId;
        return this;
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
    public Bill setBalance(BigDecimal balance) {
        mBalance = balance;
        return this;
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
    public Bill setRemark(@Nullable String remark) {
        mRemark = remark;
        return this;
    }

}
