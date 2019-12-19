package io.github.skywalkerdarren.simpleaccounting.model;

import java.math.BigDecimal;

public final class BillStats extends BaseStats {
    BillStats(BigDecimal income, BigDecimal expense) {
        super(income, expense);
    }
}
