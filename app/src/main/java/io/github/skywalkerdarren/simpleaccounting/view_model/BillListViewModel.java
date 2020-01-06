package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.StatsActivity;
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil;

/**
 * 账单列表vm
 *
 * @author darren
 * @date 2018/4/5
 */

public class BillListViewModel extends ViewModel {
    private final AppRepository mRepository;
    private final MutableLiveData<String> income = new MutableLiveData<>();
    private final MutableLiveData<String> expense = new MutableLiveData<>();
    private final MutableLiveData<String> month = new MutableLiveData<>();
    private final MutableLiveData<String> budget = new MutableLiveData<>("TODO");
    private final MutableLiveData<String> budgetText = new MutableLiveData<>("TODO");
    private final MutableLiveData<DateTime> mDateTime = new MutableLiveData<>();
    private final MutableLiveData<List<BillInfo>> billInfoList = new MutableLiveData<>();

    public BillListViewModel(AppRepository repository) {
        mRepository = repository;
    }

    /**
     * @return 日期
     */
    public LiveData<DateTime> getDate() {
        return mDateTime;
    }

    /**
     * 设置日期
     */
    public void setDate(DateTime date) {
        mDateTime.setValue(date);
        DateTime month = new DateTime(date.getYear(), date.getMonthOfYear(),
                1, 0, 0);
        mRepository.getBillStats(month, month.plusMonths(1), billStats ->
                income.setValue(FormatUtil.getNumeric(billStats.getIncome())));
        mRepository.getBillStats(month, month.plusMonths(1), billStats ->
                expense.setValue(FormatUtil.getNumeric(billStats.getExpense())));
        mRepository.getBillInfoList(date.getYear(), date.getMonthOfYear(), billsInfo ->
                billInfoList.setValue(billsInfo));
        this.month.setValue(String.valueOf(month.getMonthOfYear()));
    }

    /**
     * @return 收入
     */
    public MutableLiveData<String> getIncome() {
        return income;
    }

    /**
     * @return 支出
     */
    public MutableLiveData<String> getExpense() {
        return expense;
    }

    /**
     * @return 月份
     */
    public MutableLiveData<String> getMonth() {
        return month;
    }

    /**
     * @return 预算剩余
     */
    public MutableLiveData<String> getBudget() {
        // TODO: 2018/4/5 预算逻辑
        return budget;
    }

    /**
     * @return 当前预算
     */
    public MutableLiveData<String> getBudgetText() {
        // TODO: 2018/4/5 设置预算
        return budgetText;
    }

    /**
     * 跳转到统计页
     */
    public void toStats(Context context) {
        Intent intent = StatsActivity.newIntent(context);
        context.startActivity(intent);
    }

    public LiveData<List<BillInfo>> getBillInfoList() {
        return billInfoList;
    }
}
