package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.database.BillDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 账单详单vm
 *
 * @author darren
 * @date 2018/4/4
 */
public class BillDetailViewModel extends ViewModel implements BillDataSource.LoadBillCallBack {
    private static int mode = 0;
    private AppRepositry mRepositry;
    private DateTime mStart;
    private DateTime mEnd;
    private volatile Bill mBill;
    private MutableLiveData<Integer> statsMode = new MutableLiveData<>(R.string.brvah_loading);
    private MutableLiveData<String> typeImage = new MutableLiveData<>();
    private MutableLiveData<String> typeName = new MutableLiveData<>();
    private MutableLiveData<String> balance = new MutableLiveData<>();
    private MutableLiveData<Integer> balanceColor = new MutableLiveData<>(R.color.black);
    private MutableLiveData<String> accountName = new MutableLiveData<>();
    private MutableLiveData<String> recorder = new MutableLiveData<>("TODO");
    private MutableLiveData<String> time = new MutableLiveData<>();
    private MutableLiveData<String> remark = new MutableLiveData<>();
    private MutableLiveData<String> accountPercent = new MutableLiveData<>();
    private MutableLiveData<String> typePercent = new MutableLiveData<>();
    private MutableLiveData<String> thanAverage = new MutableLiveData<>();
    private MutableLiveData<String> typeAverage = new MutableLiveData<>();
    private MutableLiveData<Integer> thanAverageHint = new MutableLiveData<>(R.string.brvah_loading);
    private MutableLiveData<Integer> expensePercentHint = new MutableLiveData<>(R.string.brvah_loading);
    private MutableLiveData<String> expensePercent = new MutableLiveData<>();

    public BillDetailViewModel(AppRepositry repositry) {
        mRepositry = repositry;
    }

    public void start(Bill b) {
        mRepositry.getBill(b.getUUID(), this);
    }

    @Override
    public void onBillLoaded(Bill bill) {
        mBill = bill;
        int month = mBill.getDate().getMonthOfYear();
        int year = mBill.getDate().getYear();
        // 默认为月度统计
        mStart = new DateTime(year, month, 1, 0, 0);
        mEnd = mStart.plusMonths(1);
        update();
    }

    private void update() {
        mRepositry.getAccount(mBill.getAccountId(), account ->
                accountName.setValue(account.getName()));
        mRepositry.getType(mBill.getTypeId(), type -> {
            typeImage.setValue(Type.FOLDER + type.getAssetsName());
            typeName.setValue(type.getName());
            balanceColor.setValue(type.getIsExpense() ?
                    R.color.deeporange800 : R.color.lightgreen700);
            expensePercentHint.setValue(type.getIsExpense() ?
                    R.string.expense_percent : R.string.income_percent);
            mRepositry.getAccountStats(mBill.getAccountId(), mStart, mEnd, accountStats ->
                    accountPercent.setValue(type.getIsExpense() ?
                            getPercent(accountStats.getExpense()) :
                            getPercent(accountStats.getIncome())));
            mRepositry.getBillStats(mStart, mEnd, billStats ->
                    expensePercent.setValue(type.getIsExpense() ?
                            getPercent(billStats.getExpense()) :
                            getPercent(billStats.getIncome())));
        });
        mRepositry.getTypeStats(mStart, mEnd, mBill.getTypeId(), typeStats ->
                typePercent.setValue(getPercent(typeStats.getBalance())));
        mRepositry.getTypeAverage(mStart, mEnd, mBill.getTypeId(), typeStats -> {
            BigDecimal sub = mBill.getBalance().subtract(typeStats.getBalance()).abs();
            thanAverage.setValue(sub.multiply(BigDecimal.valueOf(100))
                    .divide(typeStats.getBalance(), 2, BigDecimal.ROUND_HALF_UP) + "%");
            typeAverage.setValue(FormatUtil.getNumeric(typeStats.getBalance()));
            thanAverageHint.setValue(mBill.getBalance().compareTo(typeStats.getBalance()) >= 0 ?
                    R.string.higher_than_average : R.string.less_than_average);
        });

        balance.setValue(FormatUtil.getNumeric(mBill.getBalance()));
        time.setValue(mBill.getDate().toString("yyyy-MM-dd hh:mm"));
        remark.setValue(mBill.getRemark());

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

    /**
     * 选择日期区间 0：月 1：年 2：日
     */
    public void setDate() {
        int day = mBill.getDate().getDayOfMonth();
        int month = mBill.getDate().getMonthOfYear();
        int year = mBill.getDate().getYear();
        mode %= 3;
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
        update();
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
