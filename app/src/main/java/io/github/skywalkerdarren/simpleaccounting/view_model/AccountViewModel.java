package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.databinding.BaseObservable;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * @author darren
 * @date 2018/4/4
 */

public class AccountViewModel extends BaseObservable {
    private AccountLab mAccountLab;
    private StatsLab mStatsLab;
    private StatsLab.Stats mStats;

    public AccountViewModel(Context context) {
        mStatsLab = StatsLab.getInstance(context);
        mAccountLab = AccountLab.getInstance(context);
        mStats = mStatsLab.getStats(new DateTime(0), DateTime.now());
    }

    public String getNav() {
        return mStats.getSum().toString();
    }

    public String getLiavility() {
        return mStats.getExpense().toString();
    }

    public String getTotalAssets() {
        return mStats.getIncome().toString();
    }

    public String getAccountSize() {
        return mAccountLab.getAccounts().size() + "";
    }
}
