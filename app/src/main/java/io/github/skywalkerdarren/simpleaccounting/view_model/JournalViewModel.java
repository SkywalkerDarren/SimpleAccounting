package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import kotlin.Unit;

/**
 * 流水vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class JournalViewModel extends ViewModel {
    private final MutableLiveData<BigDecimal> mIncome = new MutableLiveData<>(BigDecimal.ZERO);
    private final MutableLiveData<BigDecimal> mExpense = new MutableLiveData<>(BigDecimal.ZERO);
    private final MutableLiveData<BigDecimal> mSum = new MutableLiveData<>(BigDecimal.ZERO);
    private final MutableLiveData<String> mYear = new MutableLiveData<>();
    private final MutableLiveData<List<BillStats>> mStats = new MutableLiveData<>();
    private final AppRepository mRepository;

    public JournalViewModel(AppRepository repository) {
        mRepository = repository;
        mYear.setValue(String.valueOf(DateTime.now().getYear()));
        setStats(Integer.valueOf(Objects.requireNonNull(mYear.getValue())));
    }

    /**
     * @return 统计列表
     */
    public MutableLiveData<List<BillStats>> getStats() {
        return mStats;
    }

    private void setStats(int year) {
        mRepository.getBillsAnnualStats(year, billsStats -> {
            BigDecimal expense = BigDecimal.ZERO;
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < billsStats.size(); i++) {
                BillStats stats = billsStats.get(i);
                expense = expense.add(stats.getExpense());
                income = income.add(stats.getIncome());
                sum = sum.add(stats.getSum());
            }
            mExpense.setValue(expense);
            mIncome.setValue(income);
            mSum.setValue(sum);
            mStats.setValue(billsStats);
            return Unit.INSTANCE;
        });
    }

    /**
     * @return 收入
     */
    public MutableLiveData<BigDecimal> getIncome() {
        return mIncome;
    }

    /**
     * @return 支出
     */
    public MutableLiveData<BigDecimal> getExpense() {
        return mExpense;
    }

    /**
     * @return 盈余
     */
    public MutableLiveData<BigDecimal> getSum() {
        return mSum;
    }

    /**
     * @return 年份
     */
    public MutableLiveData<String> getDate() {
        return mYear;
    }

    /**
     * @param year 设置年份
     */
    public void setDate(int year) {
        mYear.setValue(String.valueOf(year));
    }

    /**
     * 改变日期
     */
    public void changeDate() {
        // TODO: 2018/4/6 改变日期
    }
}


