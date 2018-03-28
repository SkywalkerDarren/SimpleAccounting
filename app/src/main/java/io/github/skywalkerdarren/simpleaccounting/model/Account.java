package io.github.skywalkerdarren.simpleaccounting.model;

import android.support.annotation.DrawableRes;

import java.math.BigDecimal;

/**
 * 存钱的账户
 *
 * @author darren
 * @date 2018/3/24
 */

public class Account {
    private String mName;
    private String mBalanceHint;
    private BigDecimal mBalance;
    @DrawableRes
    private int mImageId;
    private int mColor;

    /**
     * @param name        账户名称
     * @param balanceHint 账户余额提醒
     * @param imageId     图像资源id
     * @param color       16进制的背景颜色（需要通过getResource()获得）
     */
    public Account(String name, String balanceHint, @DrawableRes int imageId, int color) {
        mName = name;
        mBalanceHint = balanceHint;
        mImageId = imageId;
        mColor = color;
        mBalance = BigDecimal.ZERO;
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
}
