package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.util.Log;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * 账户vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class AccountItemViewModel extends BaseObservable {
    private Account mAccount;
    private Context mContext;
    private static final String TAG = "AccountItemViewModel";

    public AccountItemViewModel(Account account, Context context) {
        mAccount = account;
        mContext = context;
    }

    /**
     * @return 账户图片id
     */
    public int getImg() {
        return mAccount.getImageId();
    }

    /**
     * @return 账户背景色值
     */
    public int getColor() {
        return mAccount.getColor();
    }

    /**
     * @return 账户盈余
     */
    public String getBalance() {
        Log.d(TAG, "getBalance() called");
        StatsLab lab = StatsLab.getInstance(mContext);
        StatsLab.AccountStats stats = lab.getAccountStats(mAccount.getId(),
                new DateTime(0), DateTime.now());
        // 账户基础金额 + 统计盈余
        return mAccount.getBalance().add(stats.getSum()).toString();
    }

    /**
     * @return 账户名
     */
    public String getName() {
        return mAccount.getName();
    }

    /**
     * @return 账户备注
     */
    public String getBalanceHint() {
        return mAccount.getBalanceHint();
    }

    public void test() {
        Log.d(TAG, "test() called");
    }
}
