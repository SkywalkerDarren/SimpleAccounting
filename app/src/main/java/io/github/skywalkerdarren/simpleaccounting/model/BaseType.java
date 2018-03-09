package io.github.skywalkerdarren.simpleaccounting.model;

import android.support.annotation.DrawableRes;

import java.util.List;

/**
 * Created by darren on 2018/2/12.
 * 基本账单类型
 */

public abstract class BaseType {

    protected List<BaseType> mBaseTypes;
    private String mName;
    @DrawableRes
    private int mResId;

    protected BaseType() {
    }

    public BaseType(String name, @DrawableRes int id) {
        mName = name;
        mResId = id;
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
     * 设定类型集
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
}
