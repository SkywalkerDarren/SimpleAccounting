package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.StatsActivity;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 账单列表vm
 *
 * @author darren
 * @date 2018/4/5
 */

public class BillListViewModel extends ViewModel {
    private AppRepositry mRepositry;
    private MutableLiveData<String> income = new MutableLiveData<>();
    private MutableLiveData<String> expense = new MutableLiveData<>();
    private MutableLiveData<String> month = new MutableLiveData<>();
    private MutableLiveData<String> budget = new MutableLiveData<>("TODO");
    private MutableLiveData<String> budgetText = new MutableLiveData<>("TODO");
    private MutableLiveData<DateTime> mDateTime = new MutableLiveData<>();

    public BillListViewModel(AppRepositry repositry) {
        mRepositry = repositry;
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
        mRepositry.getBillStats(month, month.plusMonths(1), billStats ->
                income.setValue(FormatUtil.getNumeric(billStats.getIncome())));
        mRepositry.getBillStats(month, month.plusMonths(1), billStats ->
                expense.setValue(FormatUtil.getNumeric(billStats.getExpense())));
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
        Context context1 = context;
        Intent intent = StatsActivity.newIntent(context);
        context.startActivity(intent);
    }
}
