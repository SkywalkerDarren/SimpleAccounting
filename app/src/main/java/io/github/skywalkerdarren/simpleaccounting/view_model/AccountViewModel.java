package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 账户页vm
 *
 * @author darren
 * @date 2018/4/4
 */

public class AccountViewModel extends BaseObservable {
    private BillStats mStats;
    private AppRepositry mRepositry;

    /**
     * 初始化lab 和列表stats
     *
     * @param context 上下文
     */
    public AccountViewModel(Context context) {
        mRepositry = AppRepositry.getInstance(new AppExecutors(), context);
        mStats = mRepositry.getBillStats(new DateTime(0), DateTime.now());
    }

    public void setStats() {
        mStats = mRepositry.getBillStats(new DateTime(0), DateTime.now());
        notifyChange();
    }

    /**
     * @return 净资产
     */
    @Bindable
    public String getNav() {
        return FormatUtil.getNumeric(mStats.getSum());
    }

    /**
     * @return 负债
     */
    @Bindable
    public String getLiability() {
        return FormatUtil.getNumeric(mStats.getExpense());
    }

    /**
     * @return 总资产
     */
    @Bindable
    public String getTotalAssets() {
        return FormatUtil.getNumeric(mStats.getIncome());
    }

    /**
     * @return 账户数目
     */
    @Bindable
    public String getAccountSize() {
        return mRepositry.getAccounts().size() + "";
    }

    public void changePosition(Account a, Account b) {
        mRepositry.changePosition(a, b);
    }
}
