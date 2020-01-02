package io.github.skywalkerdarren.simpleaccounting.model.datasource

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo
import java.util.*

interface BillDataSource {
    fun getBillsCount(callBack: LoadBillCountCallBack)
    fun getBillsCount(year: Int, month: Int, callBack: LoadBillCountCallBack)
    fun getBillInfoList(year: Int, month: Int, callBack: LoadBillsInfoCallBack)
    fun getBill(id: UUID, callBack: LoadBillCallBack)
    fun getsBills(year: Int, month: Int, callBack: LoadBillsCallBack)
    fun addBill(bill: Bill)
    fun delBill(id: UUID)
    fun updateBill(bill: Bill)
    fun clearBill()
    interface LoadBillCountCallBack {
        fun onBillCountLoaded(count: Int)
    }

    interface LoadBillCallBack {
        fun onBillLoaded(bill: Bill?)
    }

    interface LoadBillsCallBack {
        fun onBillsLoaded(bills: List<Bill>?)
    }

    interface LoadBillsInfoCallBack {
        fun onBillsInfoLoaded(billsInfo: List<BillInfo>?)
    }
}