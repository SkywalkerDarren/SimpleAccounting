package io.github.skywalkerdarren.simpleaccounting.model.entity;

import java.math.BigDecimal;

public final class AccountStats {
    private BigDecimal income = BigDecimal.ZERO;
    private BigDecimal expense = BigDecimal.ZERO;
    private BigDecimal sum = BigDecimal.ZERO;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
        sum = income.add(expense.negate());
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
        sum = income.add(expense.negate());
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "AccountStats{" +
                "income=" + income +
                ", expense=" + expense +
                ", sum=" + sum +
                '}';
    }
}
