package io.github.skywalkerdarren.simpleaccounting.model.datasource

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo
import java.util.*

interface BillDataSource {
    fun getBillsCount(callBack: (count: Int) -> Unit)
    fun getBillsCount(year: Int, month: Int, callBack: (count: Int) -> Unit)
    fun getBillInfoList(year: Int, month: Int, callBack: (billsInfo: List<BillInfo>?) -> Unit)
    fun getBill(id: UUID, callBack: (bill: Bill?) -> Unit)
    fun getsBills(year: Int, month: Int, callBack: (bills: List<Bill>?) -> Unit)
    fun addBill(bill: Bill)
    fun delBill(id: UUID)
    fun updateBill(bill: Bill)
    fun clearBill()
}