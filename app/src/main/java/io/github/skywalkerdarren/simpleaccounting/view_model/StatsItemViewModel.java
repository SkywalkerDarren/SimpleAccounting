package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;

import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * @author darren
 * @date 2018/4/6
 */

public class StatsItemViewModel extends BaseObservable {
    private StatsLab.Stats item;
    private int mMonth;

    public StatsItemViewModel(StatsLab.Stats item, int month) {
        this.item = item;
        mMonth = month;
    }

    public String getIncome() {
        return item.getIncome().toString();
    }

    public String getExpense() {
        return item.getExpense().toString();
    }

    public String getBalance() {
        return item.getSum().toString();
    }

    public String getMonth() {
        return mMonth + "";
    }
}
