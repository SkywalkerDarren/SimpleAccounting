package io.github.skywalkerdarren.simpleaccounting.model.entity;

import androidx.room.Entity;

import java.math.BigDecimal;

public class BillStats {
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal sum;

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
}
