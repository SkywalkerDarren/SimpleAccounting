package io.github.skywalkerdarren.simpleaccounting.model.entity;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.joda.time.DateTime;

import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.adapter.DateHeaderDivider;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

import static io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter.HEADER;
import static io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter.WITHOUT_REMARK;
import static io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter.WITH_REMARK;

/**
 * 账单摘要信息类
 * 用于构造适用于适配器的账单列表
 *
 * @author darren
 * @date 2018/3/17
 */

public class BillInfo implements MultiItemEntity {
    private int mType;

    private UUID mUUID;
    private String mTitle;
    private String mRemark;
    private String mBalance;
    private boolean mIsExpense;
    private String mBillTypeRes;

    private String mIncome;
    private String mExpense;

    private DateTime mDateTime;

    /**
     * 构造账单摘要
     *
     * @param bill 账单
     * @param type 类型
     */
    public BillInfo(Bill bill, Type type) {
        mType = TextUtils.isEmpty(bill.getRemark()) ? WITHOUT_REMARK : WITH_REMARK;
        mUUID = bill.getUUID();
        mTitle = bill.getName();
        mRemark = bill.getRemark();
        mBalance = FormatUtil.getNumeric(bill.getBalance());
        mIsExpense = type.getIsExpense();
        mBillTypeRes = type.getAssetsName();
        mDateTime = bill.getDate();
    }

    /**
     * 构造账单分隔符
     *
     * @param header 分隔符
     */
    public BillInfo(DateHeaderDivider header) {
        mType = HEADER;
        mDateTime = header.getDate();
        mExpense = header.getExpense();
        mIncome = header.getIncome();
    }

    @Override
    public int getItemType() {
        return mType;
    }

    /**
     * @return 账单备注 可能为空
     */
    @Nullable
    public String getRemark() {
        return mRemark;
    }

    /**
     * @return 账单类型资源id
     */
    public String getBillTypeRes() {
        return mBillTypeRes;
    }

    /**
     * @return 账单唯一id
     */
    public UUID getUUID() {
        return mUUID;
    }

    /**
     * @return 账单标题
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return 账单收支
     */
    public String getBalance() {
        return mBalance;
    }

    /**
     * 账单是否为支出
     *
     * @return true为支出
     */
    public boolean getIsExpense() {
        return mIsExpense;
    }

    /**
     * 分隔符专用
     *
     * @return 获得收入金额
     */
    public String getIncome() {
        return mIncome;
    }

    /**
     * 分隔符专用
     *
     * @return 获得支出金额
     */
    public String getExpense() {
        return mExpense;
    }

    /**
     * 分隔符专用
     *
     * @return 账单日期
     */
    public DateTime getDateTime() {
        return mDateTime;
    }

    @Override
    public String toString() {
        return "BillInfo{" +
                "mType=" + mType +
                ", mUUID=" + mUUID +
                ", mTitle='" + mTitle + '\'' +
                ", mRemark='" + mRemark + '\'' +
                ", mBalance='" + mBalance + '\'' +
                ", mIsExpense=" + mIsExpense +
                ", mBillTypeRes='" + mBillTypeRes + '\'' +
                ", mIncome='" + mIncome + '\'' +
                ", mExpense='" + mExpense + '\'' +
                ", mDateTime=" + mDateTime +
                '}';
    }
}
