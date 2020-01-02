package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.room.ColumnInfo
import java.math.BigDecimal
import java.util.*

data class TypeStats(
        @ColumnInfo(name = "type_id") val typeId: UUID,
        val balance: BigDecimal
)