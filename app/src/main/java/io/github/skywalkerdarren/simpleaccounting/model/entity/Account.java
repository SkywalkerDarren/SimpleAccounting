package io.github.skywalkerdarren.simpleaccounting.model.entity;


import androidx.annotation.ColorRes;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 存钱的账户
 *
 * @author darren
 * @date 2018/3/24
 */

public class Account {
    public static final String FOLDER = "account/";
    public static final String PNG = ".png";
    private Integer mId;
    private UUID mUUID;
    private String mName;
    private String mBalanceHint;
    private BigDecimal mBalance;
    private String mBitmap;
    private int mColor;
    @ColorRes
    private int mColorId;

    public Account(UUID uuid) {
        mUUID = uuid;
    }

    public Account() {
        mUUID = UUID.randomUUID();
    }

    public void plusBalance(BigDecimal balance) {
        mBalance = mBalance.add(balance);
    }

    public void minusBalance(BigDecimal balance) {
        mBalance = mBalance.subtract(balance);
    }

    @ColorRes
    public int getColorId() {
        return mColorId;
    }

    public Account setColorId(@ColorRes int colorId) {
        mColorId = colorId;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Account setName(String name) {
        mName = name;
        return this;
    }

    public String getBalanceHint() {
        return mBalanceHint;
    }

    public Account setBalanceHint(String balanceHint) {
        mBalanceHint = balanceHint;
        return this;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    public Account setBalance(BigDecimal balance) {
        mBalance = balance;
        return this;
    }

    public String getBitmap() {
        return mBitmap;
    }

    public Account setBitmap(String name) {
        mBitmap = name;
        return this;
    }

    public int getColor() {
        return mColor;
    }

    public Account setColor(int color) {
        mColor = color;
        return this;
    }

    public UUID getUUID() {
        return mUUID;
    }
}
