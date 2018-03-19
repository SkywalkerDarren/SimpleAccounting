package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

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
    private String mBillTypeName;
    @DrawableRes
    private int mBillTypeResId;

    private String mIncome;
    private String mExpense;

    private DateTime mDateTime;

    /**
     * 构造账单摘要
     *
     * @param bill 账单
     */
    private BillInfo(Bill bill) {
        mType = TextUtils.isEmpty(bill.getRemark()) ? WITHOUT_REMARK : WITH_REMARK;
        mUUID = bill.getId();
        mTitle = bill.getName();
        mRemark = bill.getRemark();
        mBalance = bill.getBalance().toString();
        mIsExpense = bill.isExpense();
        mBillTypeName = bill.getTypeName();
        mBillTypeResId = bill.getTypeResId();
        mDateTime = bill.getDate();
    }

    /**
     * 构造账单分隔符
     *
     * @param header 分隔符
     */
    private BillInfo(DateHeaderDivider header) {
        mType = HEADER;
        mDateTime = header.getDate();
        mExpense = header.getExpense();
        mIncome = header.getIncome();
    }

    /**
     * 获得一个月的账单摘要列表
     *
     * @param year  账单年份
     * @param month 账单月份
     * @return 账单摘要列表
     */
    public static List<BillInfo> getBillInfoList(int year, int month, Context context) {
        BillLab billLab = BillLab.getInstance(context);
        List<Bill> bills = billLab.getsBills(year, month);
        List<BillInfo> billInfoList = new ArrayList<>();
        // 上一个账单的年月日
        DateTime date = null;
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            DateTime dateTime = bill.getDate();
            int y = dateTime.getYear();
            int m = dateTime.getMonthOfYear();
            int d = dateTime.getDayOfMonth();
            // 当前账单的年月日
            DateTime currentDate = new DateTime(y, m, d, 0, 0);
            // 如果当前帐单与上一张单年月日不同，则添加账单
            if (date == null || !date.equals(currentDate)) {
                date = currentDate;
                BigDecimal income = billLab.getStats(date, date.plusDays(1)).getIncome();
                BigDecimal expense = billLab.getStats(date, date.plusDays(1)).getExpense();
                billInfoList.add(new BillInfo(new DateHeaderDivider(date, income, expense)));
            }
            billInfoList.add(new BillInfo(bill));

        }
        return billInfoList;
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
    @DrawableRes
    public int getBillTypeResId() {
        return mBillTypeResId;
    }

    /**
     * @return 获得账单类型名
     */
    public String getBillTypeName() {
        return mBillTypeName;
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
    public boolean isExpense() {
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

}
