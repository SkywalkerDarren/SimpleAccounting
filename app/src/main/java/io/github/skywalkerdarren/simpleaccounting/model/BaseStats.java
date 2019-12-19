package io.github.skywalkerdarren.simpleaccounting.model;

import java.math.BigDecimal;

abstract class BaseStats {
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal sum;

    BaseStats(BigDecimal income, BigDecimal expense) {
        this.income = income;
        this.expense = expense;
        sum = income.subtract(expense);
    }

    BaseStats(BigDecimal sum) {
        income = BigDecimal.ZERO;
        expense = BigDecimal.ZERO;
        this.sum = sum;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public BigDecimal getSum() {
        return sum;
    }
}

