package io.github.skywalkerdarren.simpleaccounting.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource.LoadBillsInfoCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.StatsDataSource.LoadBillStatsCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats
import io.github.skywalkerdarren.simpleaccounting.ui.activity.StatsActivity
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil
import org.joda.time.DateTime
import java.util.*

/**
 * 账单列表vm
 *
 * @author darren
 * @date 2018/4/5
 */
class BillListViewModel(private val mRepository: AppRepository) : ViewModel() {

    val income = MutableLiveData<String>()
    val expense = MutableLiveData<String>()
    val month = MutableLiveData<String>()
    val budget = MutableLiveData("TODO")
    val budgetText = MutableLiveData("TODO")
    private val mDateTime = MutableLiveData<DateTime>()
    private val billInfoList = MutableLiveData<List<BillInfo>>()
    val date: LiveData<DateTime>
        get() = mDateTime

    /**
     * 设置日期
     */
    fun setDate(date: DateTime) {
        mDateTime.value = date
        val month = DateTime(date.year, date.monthOfYear,
                1, 0, 0)
        mRepository.getBillStats(month, month.plusMonths(1), object : LoadBillStatsCallBack {
            override fun onBillStatsLoaded(billStats: BillStats?) {
                income.value = FormatUtil.getNumeric(billStats?.income)
            }
        })
        mRepository.getBillStats(month, month.plusMonths(1), object : LoadBillStatsCallBack {
            override fun onBillStatsLoaded(billStats: BillStats?) {
                expense.value = FormatUtil.getNumeric(billStats?.expense)
            }
        })
        mRepository.getBillInfoList(date.year, date.monthOfYear, object : LoadBillsInfoCallBack {
            override fun onBillsInfoLoaded(billsInfo: List<BillInfo>?) {
                billInfoList.value = billsInfo
            }
        })
        this.month.value = month.monthOfYear.toString()
    }

    /**
     * 跳转到统计页
     */
    fun toStats(context: Context) {
        val intent = StatsActivity.newIntent(context)
        context.startActivity(intent)
    }

    fun getBillInfoList(): LiveData<List<BillInfo>> {
        return billInfoList
    }

    fun getBill(billId: UUID, callBack: BillDataSource.LoadBillCallBack) {
        mRepository.getBill(billId, callBack)
    }

}