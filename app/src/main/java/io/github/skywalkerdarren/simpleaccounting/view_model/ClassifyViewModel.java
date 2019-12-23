package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;


/**
 * 分类页vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class ClassifyViewModel extends BaseObservable {
    private DateTime mStart;
    private DateTime mEnd;
    private Period mPeriod;
    private boolean mIsExpense;
    private Context mContext;
    private AppRepositry mRepositry;

    public ClassifyViewModel(DateTime dateTime, Context context) {
        mContext = context;
        mStart = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0);
        mEnd = mStart.plusMonths(1);
        mIsExpense = true;
        mPeriod = new Period(mStart, mEnd);
        mRepositry = AppRepositry.getInstance(new AppExecutors(), context);
    }

    /**
     * 设置日期
     *
     * @param start 起始日期
     * @param end   结束日期
     */
    public void setDate(DateTime start, DateTime end) {
        mEnd = end;
        mStart = start;
        mPeriod = new Period(mStart, mEnd);
        notifyChange();
    }

    /**
     * @return 时间区间
     */
    @Bindable
    public String getDate() {
        return mStart.toString("yyyy年MM月dd日") + " - " + mEnd.toString("yyyy年MM月dd日");
    }

    /**
     * @return 起始日期
     */
    public DateTime getStart() {
        return mStart;
    }

    /**
     * @return 结束日期
     */
    public DateTime getEnd() {
        return mEnd;
    }

    /**
     * 后退日期
     */
    public void back() {
        mEnd = mEnd.minus(mPeriod);
        mStart = mStart.minus(mPeriod);
        notifyChange();
    }

    /**
     * 前进日期
     */
    public void more() {
        mEnd = mEnd.plus(mPeriod);
        mStart = mStart.plus(mPeriod);
        notifyChange();
    }

    /**
     * @return true为支出
     */
    public boolean isExpense() {
        return mIsExpense;
    }

    /**
     * 设置是否收支
     *
     * @param expense true为支出
     */
    public void setExpense(boolean expense) {
        mIsExpense = expense;
        notifyChange();
    }

    /**
     * @return 统计列表
     */
    @Bindable
    public List<TypeStats> getStatsList() {
        return mRepositry.getTypesStats(mStart, mEnd, mIsExpense);
    }
}
