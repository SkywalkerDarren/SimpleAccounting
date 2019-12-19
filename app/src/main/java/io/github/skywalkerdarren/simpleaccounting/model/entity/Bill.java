package io.github.skywalkerdarren.simpleaccounting.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;

/**
 * 账单类
 * 记录账单详细信息
 *
 * @author darren
 * @date 2018/1/29
 */

@Entity(tableName = "bill", indices = {@Index(value = "uuid", unique = true), @Index("type_id"), @Index("account_id")}, foreignKeys = {
        @ForeignKey(entity = Type.class, parentColumns = "uuid", childColumns = "type_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(entity = Account.class, parentColumns = "uuid", childColumns = "account_id", onDelete = CASCADE, onUpdate = CASCADE)})
public class Bill implements Serializable {

    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Integer mId;

    /**
     * 账单ID号
     */
    @ColumnInfo(name = "uuid")
    private UUID mUUID;

    /**
     * 账单类型ID
     */
    @ColumnInfo(name = "type_id")
    private UUID mTypeId;

    /**
     * 账单账户类型ID
     */
    @ColumnInfo(name = "account_id")
    private UUID mAccountId;

    /**
     * 账单日期
     */
    @ColumnInfo(name = "date")
    private DateTime mDate;

    /**
     * 账单名称
     */
    @ColumnInfo(name = "name")
    private String mName;

    /**
     * 账单收支
     */
    @ColumnInfo(name = "balance")
    private BigDecimal mBalance;

    /**
     * 账单备注
     */
    @ColumnInfo(name = "remark")
    private String mRemark;

    /**
     * 通过id新建账单
     *
     * @param UUID 账单id
     */
    @Ignore
    public Bill(UUID UUID) {
        mUUID = UUID;
    }

    /**
     * 新建账单
     */
    public Bill() {
        this(UUID.randomUUID());
    }

    @Ignore
    public Bill(UUID typeId, UUID accountId, DateTime date, String name, BigDecimal balance, String remark) {
        mUUID = UUID.randomUUID();
        mTypeId = typeId;
        mAccountId = accountId;
        mDate = date;
        mName = name;
        mBalance = balance;
        mRemark = remark;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public UUID getTypeId() {
        return mTypeId;
    }

    public void setTypeId(UUID typeId) {
        mTypeId = typeId;
    }

    public UUID getAccountId() {
        return mAccountId;
    }

    public void setAccountId(UUID accountId) {
        mAccountId = accountId;
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
