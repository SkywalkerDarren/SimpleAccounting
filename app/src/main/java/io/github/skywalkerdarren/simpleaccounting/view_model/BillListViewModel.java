package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.ui.StatsActivity;

/**
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

    public void setDate(DateTime date) {
        mDateTime = date;
        mMonth = new DateTime(date.getYear(), date.getMonthOfYear(),
                1, 0, 0);
        notifyChange();
    }

    public DateTime getDate() {
        return mDateTime;
    }

    @Bindable
    public String getIncome() {
        return mStatsLab.getStats(mMonth, mMonth.plusMonths(1)).getIncome().toString();
    }

    @Bindable
    public String getExpense() {
        return mStatsLab.getStats(mMonth, mMonth.plusMonths(1)).getExpense().toString();
    }

    @Bindable
    public String getMonth() {
        return mMonth.getMonthOfYear() + "";
    }

    @Bindable
    public String getBudget() {
        // TODO: 2018/4/5 预算逻辑
        return "0";
    }

    @Bindable
    public String getBudgetText() {
        // TODO: 2018/4/5 设置预算
        return "设置预算";
    }

    public void toStats() {
        Intent intent = StatsActivity.newIntent(mContext);
        mContext.startActivity(intent);
    }
}
