package io.github.skywalkerdarren.simpleaccounting.model.entity;


import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 存钱的账户
 *
 * @author darren
 * @date 2018/3/24
 */
@Entity(tableName = "account", indices = @Index(value = "uuid", unique = true))
public class Account {
    @Ignore
    public static final String FOLDER = "account/";
    @Ignore
    public static final String PNG = ".png";
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    private Integer mId;
    @ColumnInfo(name = "uuid")
    private UUID mUUID;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "balance_hint")
    private String mBalanceHint;
    @ColumnInfo(name = "balance")
    private BigDecimal mBalance;
    @ColumnInfo(name = "image")
    private String mBitmap;
    @ColorRes
    @ColumnInfo(name = "color_id")
    private Integer mColorId;

    @Ignore
    public Account(UUID uuid) {
        mUUID = uuid;
    }

    public Account() {
        mUUID = UUID.randomUUID();
    }

    @Ignore
    public Account(String name, String balanceHint, BigDecimal balance, String bitmap, Integer colorId) {
        mUUID = UUID.randomUUID();
        mName = name;
        mBalanceHint = balanceHint;
        mBalance = balance;
        mBitmap = bitmap;
        mColorId = colorId;
    }

    public void plusBalance(BigDecimal balance) {
        mBalance = mBalance.add(balance);
    }

    public void minusBalance(BigDecimal balance) {
        mBalance = mBalance.subtract(balance);
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getBalanceHint() {
        return mBalanceHint;
    }

    public void setBalanceHint(String balanceHint) {
        mBalanceHint = balanceHint;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    public void setBalance(BigDecimal balance) {
        mBalance = balance;
    }

    public String getBitmap() {
        return mBitmap;
    }

    public void setBitmap(String bitmap) {
        mBitmap = bitmap;
    }


    public Integer getColorId() {
        return mColorId;
    }

    public void setColorId(Integer colorId) {
        mColorId = colorId;
    }
}
