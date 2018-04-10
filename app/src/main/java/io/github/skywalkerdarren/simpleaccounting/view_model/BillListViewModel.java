package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.ui.StatsActivity;

/**
 * 账单列表vm
 *
 * @author darren
 * @date 2018/4/5
 */

public class BillListViewModel extends BaseObservable {
    private StatsLab mStatsLab;
    private Context mContext;
    private DateTime mDateTime;
    private DateTime mMonth;

    public BillListViewModel(Context context) {
        mContext = context;
        mStatsLab = StatsLab.getInstance(mContext);
    }

    /**
     * @return 日期
     */
    public DateTime getDate() {
        return mDateTime;
    }

    /**
     * 设置日期
     */
    public void setDate(DateTime date) {
        mDateTime = date;
        mMonth = new DateTime(date.getYear(), date.getMonthOfYear(),
                1, 0, 0);
        notifyChange();
    }

    /**
     * @return 收入
     */
    @Bindable
    public String getIncome() {
        return mStatsLab.getStats(mMonth, mMonth.plusMonths(1)).getIncome().toString();
    }

    /**
     * @return 支出
     */
    @Bindable
    public String getExpense() {
        return mStatsLab.getStats(mMonth, mMonth.plusMonths(1)).getExpense().toString();
    }

    /**
     * @return 月份
     */
    @Bindable
    public String getMonth() {
        return mMonth.getMonthOfYear() + "";
    }

    /**
     * @return 预算剩余
     */
    @Bindable
    public String getBudget() {
        // TODO: 2018/4/5 预算逻辑
        if (mContext.getString(R.string.set_budget).equals(getBudgetText())) {
            return "0";
        }
        return "0";
    }

    /**
     * @return 当前预算
     */
    @Bindable
    public String getBudgetText() {
        // TODO: 2018/4/5 设置预算
        return mContext.getString(R.string.set_budget);
    }

    /**
     * 跳转到统计页
     */
    public void toStats() {
        Intent intent = StatsActivity.newIntent(mContext);
        mContext.startActivity(intent);
    }
}
