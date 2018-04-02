package io.github.skywalkerdarren.simpleaccounting.model;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 存钱的账户
 *
 * @author darren
 * @date 2018/3/24
 */

public class Account {
    private UUID mUUID;
    private String mName;
    private String mBalanceHint;
    private BigDecimal mBalance;
    @DrawableRes
    private int mImageId;
    private int mColor;
    @ColorRes
    private int mColorId;

    public Account(UUID uuid) {
        mUUID = uuid;
    }

    public Account() {
        mUUID = UUID.randomUUID();
    }

    public Account setName(String name) {
        mName = name;
        return this;
    }

    public Account setBalanceHint(String balanceHint) {
        mBalanceHint = balanceHint;
        return this;
    }

    public Account setBalance(BigDecimal balance) {
        mBalance = balance;
        return this;
    }

    public void plusBalance(BigDecimal balance) {
        mBalance = mBalance.add(balance);
    }

    public void minusBalance(BigDecimal balance) {
        mBalance = mBalance.subtract(balance);
    }

    public Account setImageId(int imageId) {
        mImageId = imageId;
        return this;
    }

    public Account setColor(int color) {
        mColor = color;
        return this;
    }

    public Account setColorId(@ColorRes int colorId) {
        mColorId = colorId;
        return this;
    }

    @ColorRes
    public int getColorId() {
        return mColorId;
    }

    public String getName() {
        return mName;
    }

    public String getBalanceHint() {
        return mBalanceHint;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    @DrawableRes
    public int getImageId() {
        return mImageId;
    }

    public int getColor() {
        return mColor;
    }

    public UUID getId() {
        return mUUID;
    }
}
