package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.room.Embedded

data class CurrencyAndInfo(
        @Embedded(prefix = "r_")
        val currency: Currency?,
        @Embedded(prefix = "i_")
        val currencyInfo: CurrencyInfo?
)