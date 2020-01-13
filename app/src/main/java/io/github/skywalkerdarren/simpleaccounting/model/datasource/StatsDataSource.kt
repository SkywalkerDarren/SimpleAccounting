package io.github.skywalkerdarren.simpleaccounting.model.datasource

import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats
import org.joda.time.DateTime
import java.util.*

interface StatsDataSource {
    fun getBillsAnnualStats(year: Int, callBack: (List<BillStats>?) -> Unit)
    fun getBillStats(start: DateTime, end: DateTime, callBack: (BillStats?) -> Unit)
    fun getTypesStats(start: DateTime, end: DateTime, isExpense: Boolean, callBack: (List<TypeStats>?) -> Unit)
    fun getTypeStats(start: DateTime, end: DateTime, typeId: UUID, callBack: (TypeStats?) -> Unit)
    fun getTypeAverage(start: DateTime, end: DateTime, typeId: UUID, callBack: (TypeStats?) -> Unit)
    fun getAccountStats(accountId: UUID, start: DateTime, end: DateTime, callBack: (AccountStats?) -> Unit)
}