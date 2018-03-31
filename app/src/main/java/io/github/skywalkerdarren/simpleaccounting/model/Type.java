package io.github.skywalkerdarren.simpleaccounting.model;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import java.util.UUID;

/**
 * 链式接口实现Type
 *
 * @author darren
 * @date 2018/3/31
 */

public class Type {
    private UUID mId;
    private String mName;
    @DrawableRes
    private int mResId;
    @ColorRes
    private int mColorId;
    private boolean mIsExpense;

    /**
     * 根据id创建类型
     */
    public Type(UUID id) {
        mId = id;
    }

    /**
     * 创建新类型
     */
    public Type() {
        mId = UUID.randomUUID();
    }

    /**
     * 设定类型图标资源
     */
    public Type setResId(int resId) {
        mResId = resId;
        return this;
    }

    /**
     * 获取当前类型是否为支出
     *
     * @return true为支出
     */
    public boolean getExpense() {
        return mIsExpense;
    }

    /**
     * 设定是否为支出
     *
     * @param expense true为支出
     */
    public Type setExpense(boolean expense) {
        mIsExpense = expense;
        return this;
    }

    /**
     * 获取当前类型名称
     *
     * @return 名称
     */
    public String getName() {
        return mName;
    }

    /**
     * 设定类型名称
     */
    public Type setName(String name) {
        mName = name;
        return this;
    }

    /**
     * 获取当前类型的资源id
     *
     * @return 资源id
     */
    public @DrawableRes
    int getTypeId() {
        return mResId;
    }

    /**
     * 获取当前类型的背景颜色id
     *
     * @return 颜色id
     */
    public int getColorId() {
        return mColorId;
    }

    /**
     * 设定颜色资源
     */
    public Type setColorId(int colorId) {
        mColorId = colorId;
        return this;
    }

    /**
     * 类型唯一id
     *
     * @return id
     */
    public UUID getId() {
        return mId;
    }
}
