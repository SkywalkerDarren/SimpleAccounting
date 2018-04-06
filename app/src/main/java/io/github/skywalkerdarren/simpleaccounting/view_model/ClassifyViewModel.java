package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * @author darren
 * @date 2018/4/6
 */

public class ClassifyViewModel extends BaseObservable {
    private DateTime mStart;
    private DateTime mEnd;
    private Period mPeriod;
    private boolean mIsExpense;
    private Context mContext;

    public ClassifyViewModel(DateTime dateTime, Context context) {
        mContext = context;
        mStart = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0);
        mEnd = mStart.plusMonths(1);
        mIsExpense = true;
        mPeriod = new Period(mStart, mEnd);
    }

    public void setExpense(boolean expense) {
        mIsExpense = expense;
        notifyChange();
    }

    public void setDate(DateTime start, DateTime end) {
        mEnd = end;
        mStart = start;
        mPeriod = new Period(mStart, mEnd);
        notifyChange();
    }

    @Bindable
    public String getDate() {
        return mStart.toString("yyyy年MM月dd日") + " - " + mEnd.toString("yyyy年MM月dd日");
    }

    public DateTime getStart() {
        return mStart;
    }

    public DateTime getEnd() {
        return mEnd;
    }

    public void back() {
        mEnd = mEnd.minus(mPeriod);
        mStart = mStart.minus(mPeriod);
        notifyChange();
    }

    public void more() {
        mEnd = mEnd.plus(mPeriod);
        mStart = mStart.plus(mPeriod);
        notifyChange();
    }

    public boolean isExpense() {
        return mIsExpense;
    }

    @Bindable
    public List<StatsLab.TypeStats> getStatsList() {
        return StatsLab.getInstance(mContext).getTypeStats(mStart, mEnd, mIsExpense);
    }
}
