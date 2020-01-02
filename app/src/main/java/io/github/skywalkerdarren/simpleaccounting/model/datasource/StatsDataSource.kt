package io.github.skywalkerdarren.simpleaccounting.model.datasource

import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats
import org.joda.time.DateTime
import java.util.*

interface StatsDataSource {
    fun getBillsAnnualStats(year: Int, callBack: LoadBillsStatsCallBack)
    fun getBillStats(start: DateTime, end: DateTime, callBack: LoadBillStatsCallBack)
    fun getTypesStats(start: DateTime, end: DateTime, isExpense: Boolean, callBack: LoadTypesStatsCallBack)
    fun getTypeStats(start: DateTime, end: DateTime, typeId: UUID, callBack: LoadTypeStatsCallBack)
    fun getTypeAverage(start: DateTime, end: DateTime, typeId: UUID, callBack: LoadTypeStatsCallBack)
    fun getAccountStats(accountId: UUID, start: DateTime, end: DateTime, callBack: LoadAccountStatsCallBack)
    interface LoadBillStatsCallBack {
        fun onBillStatsLoaded(billStats: BillStats?)
    }

    interface LoadBillsStatsCallBack {
        fun onBillStatsLoaded(billsStats: List<BillStats>?)
    }

    interface LoadTypeStatsCallBack {
        fun onTypeStatsLoaded(typeStats: TypeStats?)
    }

    interface LoadTypesStatsCallBack {
        fun onTypesStatsLoaded(typesStats: List<TypeStats>?)
    }

    interface LoadAccountStatsCallBack {
        fun onAccountStatsLoaded(accountStats: AccountStats?)
    }
}