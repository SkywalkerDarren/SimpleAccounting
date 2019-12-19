package io.github.skywalkerdarren.simpleaccounting.model;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

public final class TypeStats extends BaseStats {
    private Type mType;

    TypeStats(Type type, BigDecimal balance) {
        super(balance);
        mType = type;
    }

    public Type getType() {
        return mType;
    }

    @Deprecated
    @Override
    public BigDecimal getIncome() {
        return getSum();
    }

    @Deprecated
    @Override
    public BigDecimal getExpense() {
        return getSum();
    }
}
