package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.dao.StatsDao;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

import static io.github.skywalkerdarren.simpleaccounting.model.entity.Account.FOLDER;

/**
 * 账户vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class AccountItemViewModel extends BaseObservable {
    private static final String TAG = "AccountItemViewModel";
    private Account mAccount;
    private Context mContext;

    public AccountItemViewModel(Account account, Context context) {
        mAccount = account;
        mContext = context;
    }

    /**
     * @return 账户图片id
     */
    public String getImg() {
        return FOLDER + mAccount.getBitmap();
    }

    /**
     * @return 账户背景色值
     */
    public int getColor() {
        return mContext.getResources().getColor(mAccount.getColorId());
    }

    /**
     * @return 账户盈余
     */
    public String getBalance() {
        Log.d(TAG, "getBalance() called");

        AppRepositry repositry = AppRepositry.getInstance(mContext);
        AccountStats stats = repositry.getAccountStats(mAccount.getUUID(),
                new DateTime(0), DateTime.now());
        // 账户基础金额 + 统计盈余
        return FormatUtil.getNumeric(mAccount.getBalance().add(stats.getSum()));
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
}
