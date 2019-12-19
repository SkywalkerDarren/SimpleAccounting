package io.github.skywalkerdarren.simpleaccounting.model.entity;

import java.math.BigDecimal;

public class Stats {
    private BigDecimal balance;
    private Boolean expense;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getExpense() {
        return expense;
    }

    public void setExpense(Boolean expense) {
        this.expense = expense;
    }
}
