package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;

import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * 统计项目vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class StatsItemViewModel extends BaseObservable {
    private StatsLab.Stats mStats;
    private int mMonth;

    public StatsItemViewModel(StatsLab.Stats mStats, int month) {
        this.mStats = mStats;
        mMonth = month;
    }

    /**
     * @return 收入
     */
    public String getIncome() {
        return mStats.getIncome().toString();
    }

    /**
     * @return 支出
     */
    public String getExpense() {
        return mStats.getExpense().toString();
    }

    /**
     * @return 盈余
     */
    public String getBalance() {
        return mStats.getSum().toString();
    }

    /**
     * @return 年份
     */
    public String getMonth() {
        return mMonth + "";
    }
}
