package io.github.skywalkerdarren.simpleaccounting.model;

import java.math.BigDecimal;

public final class AccountStats extends BaseStats {
    AccountStats(BigDecimal income, BigDecimal expense) {
        super(income, expense);
    }
}
