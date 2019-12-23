package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 流水vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class JournalViewModel extends BaseObservable {
    private Context mContext;
    private BigDecimal mIncome;
    private BigDecimal mExpense;
    private BigDecimal mSum;
    private int mYear;
    private List<BillStats> mStats;
    private AppRepositry mRepositry;

    public JournalViewModel(Context context) {
        mContext = context;
        mRepositry = AppRepositry.getInstance(new AppExecutors(), context);
        mYear = DateTime.now().getYear();
        mStats = mRepositry.getAnnualStats(DateTime.now().getYear());
        mIncome = BigDecimal.ZERO;
        mExpense = BigDecimal.ZERO;
        mSum = BigDecimal.ZERO;
        // 初始化求和
        for (int i = 0; i < mStats.size(); i++) {
            BillStats stats = mStats.get(i);
            mExpense = mExpense.add(stats.getExpense());
            mIncome = mIncome.add(stats.getIncome());
            mSum = mSum.add(stats.getSum());
        }
    }

    /**
     * @return 统计列表
     */
    public List<BillStats> getStats() {
        return mStats;
    }

    /**
     * @return 收入
     */
    @Bindable
    public String getIncome() {
        return FormatUtil.getNumeric(mIncome);
    }

    /**
     * @return 支出
     */
    @Bindable
    public String getExpense() {
        return FormatUtil.getNumeric(mExpense);
    }

    /**
     * @return 盈余
     */
    @Bindable
    public String getSum() {
        return FormatUtil.getNumeric(mSum);
    }

    /**
     * @return 年份
     */
    @Bindable
    public String getDate() {
        return mYear + "";
    }

    /**
     * @param year 设置年份
     */
    public void setDate(int year) {
        mYear = year;
        notifyChange();
    }

    /**
     * 改变日期
     */
    public void changeDate() {
        // TODO: 2018/4/6 改变日期
        Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
    }
}


