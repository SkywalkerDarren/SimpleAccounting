package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.databinding.BaseObservable;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * 账户页vm
 *
 * @author darren
 * @date 2018/4/4
 */

public class AccountViewModel extends BaseObservable {
    private AccountLab mAccountLab;
    private StatsLab.BillStats mStats;

    /**
     * 初始化lab 和列表stats
     *
     * @param context 上下文
     */
    public AccountViewModel(Context context) {
        StatsLab statsLab = StatsLab.getInstance(context);
        mAccountLab = AccountLab.getInstance(context);
        mStats = statsLab.getStats(new DateTime(0), DateTime.now());
    }

    /**
     * @return 净资产
     */
    public String getNav() {
        return mStats.getSum().toString();
    }

    /**
     * @return 负债
     */
    public String getLiavility() {
        return mStats.getExpense().toString();
    }

    /**
     * @return 总资产
     */
    public String getTotalAssets() {
        return mStats.getIncome().toString();
    }

    /**
     * @return 账户数目
     */
    public String getAccountSize() {
        return mAccountLab.getAccounts().size() + "";
    }
}
