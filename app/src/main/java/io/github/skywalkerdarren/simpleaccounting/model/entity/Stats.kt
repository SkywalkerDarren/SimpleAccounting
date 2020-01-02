package io.github.skywalkerdarren.simpleaccounting.model.entity

import java.math.BigDecimal

data class Stats constructor(
        var balance: BigDecimal,
        var expense: Boolean)