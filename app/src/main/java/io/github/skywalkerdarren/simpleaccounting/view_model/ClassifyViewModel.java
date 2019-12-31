package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;


/**
 * 分类页vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class ClassifyViewModel extends ViewModel {
    private DateTime mStart;
    private DateTime mEnd;
    private Period mPeriod;
    private boolean mIsExpense;
    private AppRepository mRepository;

    private MutableLiveData<String> date = new MutableLiveData<>();
    private MutableLiveData<List<TypeStats>> statsList = new MutableLiveData<>();

    public ClassifyViewModel(AppRepository repository) {
        mRepository = repository;
        mIsExpense = true;
        DateTime now = DateTime.now();
        mStart = new DateTime(now.getYear(), now.getMonthOfYear(), 1, 0, 0);
        mEnd = mStart.plusMonths(1);
        mPeriod = new Period(mStart, mEnd);
    }

    /**
     * 设置日期
     *
     * @param end 结束日期
     */
    public void setDate(DateTime start, DateTime end) {
        mEnd = end;
        mStart = start;
        mPeriod = new Period(mStart, mEnd);
        setStatsList(mStart, mEnd, mIsExpense);
    }

    public void start() {
        setStatsList(mStart, mEnd, mIsExpense);
    }

    private void setStatsList(DateTime start, DateTime end, boolean isExpense) {
        String pattern = "yyyy年MM月dd日";
        date.setValue(mStart.toString(pattern) + " - " + mEnd.toString(pattern));
        mRepository.getTypesStats(start, end, isExpense, typesStats -> statsList.setValue(typesStats));
    }

    /**
     * @return 时间区间
     */
    public MutableLiveData<String> getDate() {
        return date;
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
    }

    /**
     * 前进日期
     */
    public void more() {
        mEnd = mEnd.plus(mPeriod);
        mStart = mStart.plus(mPeriod);
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
    }

    /**
     * @return 统计列表
     */
    public LiveData<List<TypeStats>> getStatsList() {
        return statsList;
    }
}
