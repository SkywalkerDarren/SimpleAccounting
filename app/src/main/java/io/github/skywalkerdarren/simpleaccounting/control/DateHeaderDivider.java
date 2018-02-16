package io.github.skywalkerdarren.simpleaccounting.control;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by darren on 2018/2/16.
 */

public class DateHeaderDivider implements HeaderDivider {
    private DateTime mDateTime;
    private int y;
    private int m;
    private int d;
    private BigDecimal mIncome;
    private BigDecimal mExpense;

    public DateHeaderDivider(DateTime dateTime, BigDecimal income, BigDecimal expense) {
        y = dateTime.getYear();
        m = dateTime.getMonthOfYear();
        d = dateTime.getDayOfMonth();
        mDateTime = new DateTime(y, m, d, 0, 0);
        mIncome = income;
        mExpense = expense;
    }

    public DateTime getDate() {
        return mDateTime;
    }

    @Override
    public String getExpense() {
        return mExpense.toString();
    }

    @Override
    public String getIncome() {
        return mIncome.toString();
    }
}
