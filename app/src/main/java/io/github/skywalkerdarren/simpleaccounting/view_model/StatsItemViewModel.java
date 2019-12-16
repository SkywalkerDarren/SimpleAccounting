package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.annotation.IntRange;
import androidx.databinding.BaseObservable;

import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 统计项目vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class StatsItemViewModel extends BaseObservable {
    private StatsLab.BillStats mStats;
    private int mMonth;

    /**
     * @param month 从1开始
     */
    public StatsItemViewModel(StatsLab.BillStats mStats, @IntRange(from = 1, to = 12) int month) {
        this.mStats = mStats;
        mMonth = month;
    }

    /**
     * @return 收入
     */
    public String getIncome() {
        return FormatUtil.getNumeric(mStats.getIncome());
    }

    /**
     * @return 支出
     */
    public String getExpense() {
        return FormatUtil.getNumeric(mStats.getExpense());
    }

    /**
     * @return 盈余
     */
    public String getBalance() {
        return FormatUtil.getNumeric(mStats.getSum());
    }

    /**
     * @return 年份
     */
    public String getMonth() {
        return mMonth + "";
    }
}
