package io.github.skywalkerdarren.simpleaccounting.model.entity

import java.math.BigDecimal

data class AccountStats @JvmOverloads constructor(
        var income: BigDecimal = BigDecimal.ZERO,
        var expense: BigDecimal = BigDecimal.ZERO
) {
    val sum
        get() = income.add(expense.negate())
}