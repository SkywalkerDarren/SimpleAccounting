package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.model.Type;
import io.github.skywalkerdarren.simpleaccounting.model.TypeLab;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 账单详单vm
 *
 * @author darren
 * @date 2018/4/4
 */

public class BillDetailViewModel extends BaseObservable {
    private static final String TAG = "BillDetailViewModel";
    private static int mode = 0;
    private Bill mBill;
    private Type mType;
    private Account mAccount;
    private Activity mActivity;
    private StatsLab mStatsLab;
    private DateTime mStart;
    private DateTime mEnd;

    public BillDetailViewModel(Bill bill, Activity activity) {
        mActivity = activity;
        mBill = bill;
        mAccount = AccountLab.getInstance(mActivity).getAccount(mBill.getAccountId());
        mType = TypeLab.getInstance(mActivity).getType(mBill.getTypeId());
        mStatsLab = StatsLab.getInstance(mActivity);
        int month = mBill.getDate().getMonthOfYear();
        int year = mBill.getDate().getYear();
        // 默认为月度统计
        mStart = new DateTime(year, month, 1, 0, 0);
        mEnd = mStart.plusMonths(1);
    }

    /**
     * 选择日期区间 0：月 1：年 2：日
     */
    public void setDate() {
        int day = mBill.getDate().getDayOfMonth();
        int month = mBill.getDate().getMonthOfYear();
        int year = mBill.getDate().getYear();
        mode %= 3;
        Log.d(TAG, "setDate: " + mode);
        switch (mode) {
            case 0:
                mStart = new DateTime(year, month, 1, 0, 0);
                mEnd = mStart.plusMonths(1);
                break;
            case 1:
                mStart = new DateTime(year, 1, 1, 0, 0);
                mEnd = mStart.plusYears(1);
                break;
            case 2:
                mStart = new DateTime(year, month, day, 0, 0);
                mEnd = mStart.plusDays(1);
                break;
            default:
                break;
        }
        notifyChange();
    }

    @Bindable
    public String getMode() {
        String str = null;
        switch (mode %= 3) {
            case 0:
                str = mActivity.getString(R.string.monthly_stats);
                break;
            case 1:
                str = mActivity.getString(R.string.annual_stats);
                break;
            case 2:
                str = mActivity.getString(R.string.day_stats);
                break;
            default:
                break;
        }
        mode++;
        return str;
    }

    /**
     * @return 类型图id
     */
    public String getTypeImage() {
        return Type.FOLDER + mType.getAssetsName();
    }

    /**
     * @return 类型名
     */
    public String getTypeName() {
        return mType.getName();
    }

    /**
     * @return 账单收支
     */
    public String getBalance() {
        return FormatUtil.getNumeric(mBill.getBalance());
    }

    /**
     * @return 收支颜色
     */
    public int getBalanceColor() {
        return mActivity.getResources().getColor(mType.getExpense() ?
                R.color.deeporange800 :
                R.color.lightgreen700);
    }

    /**
     * @return 帐户名
     */
    public String getAccountName() {
        return mAccount.getName();
    }

    /**
     * @return 账单记录者
     */
    public String getRecorder() {
        // TODO: 2018/4/4 记录人空缺
        return "暂无";
    }

    /**
     * @return 账单日期
     */
    public String getTime() {
        return mBill.getDate().toString("yyyy-MM-dd hh:mm");
    }

    /**
     * @return 账单备注
     */
    public String getRemark() {
        return mBill.getRemark();
    }

    /**
     * 当前账单所占百分数
     *
     * @param bigDecimal 被除数
     * @return 百分数，带百分号
     */
    private String getPercent(BigDecimal bigDecimal) {
        BigDecimal balance = mBill.getBalance();
        // 确保balance小于bigDecimal
        if (balance.compareTo(bigDecimal) > 0) {
            BigDecimal t = balance;
            balance = bigDecimal;
            bigDecimal = t;
        }
        return balance.multiply(BigDecimal.valueOf(100))
                .divide(bigDecimal, 2, BigDecimal.ROUND_HALF_UP) + "%";
    }

    @Bindable
    public String getAccountPercent() {
        // 当前支出/收入，在当前时间段内，占当前账户的支出/收入百分比
        Log.d(TAG, "getAccountPercent: " + mStart.toString());
        StatsLab.AccountStats stats = mStatsLab.getAccountStats(mBill.getAccountId(), mStart, mEnd);
        return mType.getExpense() ? getPercent(stats.getExpense()) : getPercent(stats.getIncome());
    }

    @Bindable
    public String getTypePercent() {
        BigDecimal sum = mStatsLab.getTypeStats(mStart, mEnd, mBill.getTypeId());
        return getPercent(sum);
    }

    @Bindable
    public String getThanAverage() {
        BigDecimal avg = mStatsLab.getTypeAverage(mStart, mEnd, mBill.getTypeId());
        BigDecimal sub = mBill.getBalance().subtract(avg).abs();
        return sub.multiply(BigDecimal.valueOf(100))
                .divide(avg, 2, BigDecimal.ROUND_HALF_UP) + "%";
    }

    @Bindable
    public String getTypeAverage() {
        BigDecimal avg = mStatsLab.getTypeAverage(mStart, mEnd, mBill.getTypeId());
        return FormatUtil.getNumeric(avg);
    }

    @Bindable
    public String getThanAverageHint() {
        BigDecimal avg = mStatsLab.getTypeAverage(mStart, mEnd, mBill.getTypeId());
        if (mBill.getBalance().compareTo(avg) >= 0) {
            // 大于等于
            return mActivity.getString(R.string.higher_than_average);
        } else {
            return mActivity.getString(R.string.less_than_average);
        }
    }

    @Bindable
    public String getExpensePercent() {
        StatsLab.BillStats stats = mStatsLab.getStats(mStart, mEnd);
        return mType.getExpense() ? getPercent(stats.getExpense()) : getPercent(stats.getIncome());
    }

    @Bindable
    public String getExpensePercentHint() {
        return mType.getExpense() ? mActivity.getString(R.string.expense_percent) :
                mActivity.getString(R.string.income_percent);
    }

    /**
     * 编辑账单点击事件
     */
    @SuppressWarnings("unchecked")
    public void onEditFabClick(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = (int) view.getX() + view.getWidth() / 2;
        int y = (int) view.getY() + view.getHeight() / 2;
        Intent intent = BillEditActivity.newIntent(mActivity, mBill, x, y);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity);
        mActivity.startActivity(intent, options.toBundle());
    }
}
