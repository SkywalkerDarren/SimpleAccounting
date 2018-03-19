package io.github.skywalkerdarren.simpleaccounting.model;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * 基本账单类型
 *
 * @author darren
 * @date 2018/2/12
 */

public abstract class BaseType {

    private List<BaseType> mBaseTypes;
    private String mName;
    @DrawableRes
    private int mResId;
    @ColorRes
    private int mColorId;

    /**
     * 必要的空构造方法
     */
    protected BaseType() {
    }

    /**
     * 通过名称，资源id，构造新的类型
     *
     * @param name 类型名称
     * @param id   对应资源id
     */
    public BaseType(String name, @DrawableRes int id) {
        this(name, id, R.color.amber500);
    }

    /**
     * 通过名称，资源id，构造新的类型
     *
     * @param name    类型名称
     * @param resId   对应资源id
     * @param colorId 对应背景颜色id
     */
    public BaseType(String name, @DrawableRes int resId, @ColorRes int colorId) {
        mName = name;
        mResId = resId;
        mColorId = colorId;
    }


    /**
     * 获取类型的所有列表
     *
     * @return 类型列表
     */
    public List<BaseType> getTypes() {
        if (mBaseTypes == null) {
            mBaseTypes = setDefaultTypes();
        }
        return mBaseTypes;
    }

    /**
     * 设定默认类型集
     *
     * @return 类型集
     */
    protected abstract List<BaseType> setDefaultTypes();

    /**
     * 获取当前类型是否为支出
     *
     * @return true为支出
     */
    public abstract boolean getExpense();

    /**
     * 获取当前类型名称
     *
     * @return 名称
     */
    public String getName() {
        return mName;
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
}
