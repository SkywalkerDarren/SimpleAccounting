package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Objects;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil;

/**
 * 账单详单vm
 *
 * @author darren
 * @date 2018/4/4
 */
public class BillDetailViewModel extends ViewModel implements BillDataSource.LoadBillCallBack {
    private static int mode = 0;
    private final AppRepository mRepository;
    private final MutableLiveData<Integer> statsMode = new MutableLiveData<>(R.string.loading);
    private final MutableLiveData<String> typeImage = new MutableLiveData<>();
    private final MutableLiveData<String> typeName = new MutableLiveData<>("加载中...");
    private final MutableLiveData<String> balance = new MutableLiveData<>("加载中...");
    private final MutableLiveData<Integer> balanceColor = new MutableLiveData<>(R.color.white);
    private final MutableLiveData<String> accountName = new MutableLiveData<>("加载中...");
    private final MutableLiveData<String> recorder = new MutableLiveData<>("TODO");
    private final MutableLiveData<String> time = new MutableLiveData<>("加载中...");
    private final MutableLiveData<String> remark = new MutableLiveData<>("加载中...");
    private final MutableLiveData<String> accountPercent = new MutableLiveData<>("加载中...");
    private final MutableLiveData<String> typePercent = new MutableLiveData<>("加载中...");
    private final MutableLiveData<String> thanAverage = new MutableLiveData<>("加载中...");
    private final MutableLiveData<String> typeAverage = new MutableLiveData<>("加载中...");
    private final MutableLiveData<Integer> thanAverageHint = new MutableLiveData<>(R.string.brvah_loading);
    private final MutableLiveData<Integer> expensePercentHint = new MutableLiveData<>(R.string.brvah_loading);
    private final MutableLiveData<String> expensePercent = new MutableLiveData<>("加载中...");
    private final MutableLiveData<Bill> bill = new MutableLiveData<>();

    public BillDetailViewModel(AppRepository repository) {
        mRepository = repository;
    }

    public void start(Bill b) {
        mRepository.getBill(b.getUuid(), this);
    }

    private void setLoading() {
        accountPercent.setValue("加载中...");
        typePercent.setValue("加载中...");
        thanAverageHint.setValue(R.string.brvah_loading);
        thanAverage.setValue("加载中...");
        typeAverage.setValue("加载中...");
        expensePercent.setValue("加载中...");
        expensePercentHint.setValue(R.string.brvah_loading);
    }

    @Override
    public void onBillLoaded(Bill bill) {
        this.bill.setValue(bill);
        int month = bill.getDate().getMonthOfYear();
        int year = bill.getDate().getYear();
        // 默认为月度统计
        DateTime start = new DateTime(year, month, 1, 0, 0);
        DateTime end = start.plusMonths(1);
        update(bill, start, end);
    }

    private void update(Bill bill, DateTime start, DateTime end) {
        mRepository.getAccount(bill.getAccountId(), account ->
                accountName.setValue(account.getName()));
        mRepository.getType(bill.getTypeId(), type -> {
            typeImage.setValue(type.getAssetsName());
            typeName.setValue(type.getName());
            balanceColor.setValue(type.isExpense() ?
                    R.color.deeporange800 : R.color.lightgreen700);
            expensePercentHint.setValue(type.isExpense() ?
                    R.string.expense_percent : R.string.income_percent);
            mRepository.getAccountStats(bill.getAccountId(), start, end, accountStats ->
                    accountPercent.setValue(type.isExpense() ?
                            getPercent(bill, accountStats.getExpense()) :
                            getPercent(bill, accountStats.getIncome())));
            mRepository.getBillStats(start, end, billStats ->
                    expensePercent.setValue(type.isExpense() ?
                            getPercent(bill, billStats.getExpense()) :
                            getPercent(bill, billStats.getIncome())));
        });
        mRepository.getTypeStats(start, end, bill.getTypeId(), typeStats ->
                typePercent.setValue(getPercent(bill, typeStats.getBalance())));
        mRepository.getTypeAverage(start, end, bill.getTypeId(), typeStats -> {
            BigDecimal sub = bill.getBalance().subtract(typeStats.getBalance()).abs();
            try {
                thanAverage.setValue(sub.multiply(BigDecimal.valueOf(100))
                        .divide(typeStats.getBalance(), 2, BigDecimal.ROUND_HALF_UP) + "%");
            } catch (ArithmeticException e) {
                thanAverage.setValue("ERROR");
            }

            typeAverage.setValue(FormatUtil.getNumeric(typeStats.getBalance()));
            thanAverageHint.setValue(bill.getBalance().compareTo(typeStats.getBalance()) >= 0 ?
                    R.string.higher_than_average : R.string.less_than_average);
        });

        balance.setValue(FormatUtil.getNumeric(bill.getBalance()));
        time.setValue(bill.getDate().toString("yyyy-MM-dd hh:mm"));
        remark.setValue(bill.getRemark());

        switch (mode %= 3) {
            case 0:
                statsMode.setValue(R.string.monthly_stats);
                break;
            case 1:
                statsMode.setValue(R.string.annual_stats);
                break;
            case 2:
                statsMode.setValue(R.string.day_stats);
                break;
            default:
                break;
        }
        mode++;
    }

    public MutableLiveData<Bill> getBill() {
        return bill;
    }

    /**
     * 选择日期区间 0：月 1：年 2：日
     */
    public void setDate() {
        setLoading();
        DateTime start;
        DateTime end;
        int day = Objects.requireNonNull(bill.getValue()).getDate().getDayOfMonth();
        int month = bill.getValue().getDate().getMonthOfYear();
        int year = bill.getValue().getDate().getYear();
        mode %= 3;
        switch (mode) {
            case 0:
                start = new DateTime(year, month, 1, 0, 0);
                end = start.plusMonths(1);
                update(bill.getValue(), start, end);
                break;
            case 1:
                start = new DateTime(year, 1, 1, 0, 0);
                end = start.plusYears(1);
                update(bill.getValue(), start, end);
                break;
            case 2:
                start = new DateTime(year, month, day, 0, 0);
                end = start.plusDays(1);
                update(bill.getValue(), start, end);
                break;
            default:
                break;
        }
    }

    public MutableLiveData<Integer> getMode() {
        return statsMode;
    }

    /**
     * @return 类型图id
     */
    public MutableLiveData<String> getTypeImage() {
        return typeImage;
    }

    /**
     * @return 类型名
     */
    public MutableLiveData<String> getTypeName() {
        return typeName;
    }

    /**
     * @return 账单收支
     */
    public MutableLiveData<String> getBalance() {
        return balance;
    }

    /**
     * @return 收支颜色
     */
    public MutableLiveData<Integer> getBalanceColor() {
        return balanceColor;
    }

    /**
     * @return 帐户名
     */
    public MutableLiveData<String> getAccountName() {
        return accountName;
    }

    /**
     * @return 账单记录者
     */
    public MutableLiveData<String> getRecorder() {
        // TODO: 2018/4/4 记录人空缺
        return recorder;
    }

    /**
     * @return 账单日期
     */
    public MutableLiveData<String> getTime() {
        return time;
    }

    /**
     * @return 账单备注
     */
    public MutableLiveData<String> getRemark() {
        return remark;
    }

    /**
     * 当前账单所占百分数
     *
     * @param bigDecimal 被除数
     * @return 百分数，带百分号
     */
    private String getPercent(Bill bill, BigDecimal bigDecimal) {
        BigDecimal balance = bill.getBalance();
        // 确保balance小于bigDecimal
        if (balance.compareTo(bigDecimal) > 0) {
            BigDecimal t = balance;
            balance = bigDecimal;
            bigDecimal = t;
        }
        try {
            return balance.multiply(BigDecimal.valueOf(100))
                    .divide(bigDecimal, 2, BigDecimal.ROUND_HALF_UP) + "%";
        } catch (ArithmeticException e) {
            return "ERROR";
        }
    }

    public MutableLiveData<String> getAccountPercent() {
        return accountPercent;
    }

    public MutableLiveData<String> getTypePercent() {
        return typePercent;
    }

    public MutableLiveData<String> getThanAverage() {
        return thanAverage;
    }

    public MutableLiveData<String> getTypeAverage() {
        return typeAverage;
    }

    public MutableLiveData<Integer> getThanAverageHint() {
        return thanAverageHint;
    }

    public MutableLiveData<String> getExpensePercent() {
        return expensePercent;
    }

    public MutableLiveData<Integer> getExpensePercentHint() {
        return expensePercentHint;
    }
}
