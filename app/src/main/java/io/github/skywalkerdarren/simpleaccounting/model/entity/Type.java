package io.github.skywalkerdarren.simpleaccounting.model.entity;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

/**
 * 链式接口实现Type
 *
 * @author darren
 * @date 2018/3/31
 */

@Entity(tableName = "type", indices = @Index(value = "uuid", unique = true))
public class Type {
    @Ignore
    public static final String FOLDER = "type/";
    @Ignore
    public static final String PNG = ".png";
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer mId;
    @ColumnInfo(name = "uuid")
    private UUID mUUID;
    @ColumnInfo(name = "name")
    private String mName;
    @ColorRes
    @ColumnInfo(name = "color_id")
    private int mColorId;
    @ColumnInfo(name = "is_expense")
    private boolean mIsExpense;
    @ColumnInfo(name = "assets_name")
    private String mAssetsName;
    /**
     * 根据id创建类型
     */
    @Ignore
    public Type(UUID id) {
        mUUID = id;
    }

    /**
     * 创建新类型
     */
    public Type() {
        mUUID = UUID.randomUUID();
    }

    @Ignore
    public Type(String name, int colorId, boolean isExpense, String assetsName) {
        mUUID = UUID.randomUUID();
        mName = name;
        mColorId = colorId;
        mIsExpense = isExpense;
        mAssetsName = assetsName;
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

    public int getColorId() {
        return mColorId;
    }

    public void setColorId(int colorId) {
        mColorId = colorId;
    }

    public boolean getIsExpense() {
        return mIsExpense;
    }

    public void setIsExpense(boolean expense) {
        mIsExpense = expense;
    }

    public String getAssetsName() {
        return mAssetsName;
    }

    public void setAssetsName(String assetsName) {
        mAssetsName = assetsName;
    }

    @Override
    public String toString() {
        return "Type{" +
                "mId=" + mId +
                ", mUUID=" + mUUID +
                ", mName='" + mName + '\'' +
                ", mColorId=" + mColorId +
                ", mIsExpense=" + mIsExpense +
                ", mAssetsName='" + mAssetsName + '\'' +
                '}';
    }
}
