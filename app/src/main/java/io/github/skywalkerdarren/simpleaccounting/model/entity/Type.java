package io.github.skywalkerdarren.simpleaccounting.model.entity;

import androidx.annotation.ColorRes;

import java.util.UUID;

/**
 * 链式接口实现Type
 *
 * @author darren
 * @date 2018/3/31
 */

public class Type {
    public static final String FOLDER = "type/";
    public static final String PNG = ".png";
    private Integer mId;
    private UUID mUUID;
    private String mName;
    @ColorRes
    private int mColorId;
    private boolean mIsExpense;
    private String mAssetsName;

    /**
     * 根据id创建类型
     */
    public Type(UUID id) {
        mUUID = id;
    }

    /**
     * 创建新类型
     */
    public Type() {
        mUUID = UUID.randomUUID();
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
    public String getAssetsName() {
        return mAssetsName;
    }

    /**
     * 设定类型图标资源
     */
    public Type setAssetsName(String path) {
        mAssetsName = path;
        return this;
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
    public UUID getUUID() {
        return mUUID;
    }
}
