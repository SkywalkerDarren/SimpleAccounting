package io.github.skywalkerdarren.simpleaccounting.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.datasource.AccountDataSource.LoadAccountCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource.LoadBillCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.StatsDataSource.*
import io.github.skywalkerdarren.simpleaccounting.model.datasource.TypeDataSource.LoadTypeCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.*
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.*

/**
 * 账单详单vm
 *
 * @author darren
 * @date 2018/4/4
 */
class BillDetailViewModel(private val mRepository: AppRepository) : ViewModel(), LoadBillCallBack {
    private var mode = 0
    val modeText = MutableLiveData(R.string.monthly_stats)
    val typeImage = MutableLiveData<String>()
    val typeName = MutableLiveData("加载中...")
    val balance = MutableLiveData("加载中...")
    val balanceColor = MutableLiveData(R.color.white)
    val accountName = MutableLiveData("加载中...")
    val time = MutableLiveData("加载中...")
    val remark = MutableLiveData("加载中...")
    val accountPercent = MutableLiveData("加载中...")
    val typePercent = MutableLiveData("加载中...")
    val thanAverage = MutableLiveData("加载中...")
    val typeAverage = MutableLiveData("加载中...")
    val thanAverageHint = MutableLiveData(R.string.brvah_loading)
    val expensePercentHint = MutableLiveData(R.string.brvah_loading)
    val expensePercent = MutableLiveData("加载中...")
    val bill = MutableLiveData<Bill?>()
    fun start(b: Bill) {
        mRepository.getBill(b.uuid, this)
    }

    private fun setLoading() {
        accountPercent.value = "加载中..."
        typePercent.value = "加载中..."
        thanAverageHint.value = R.string.brvah_loading
        thanAverage.value = "加载中..."
        typeAverage.value = "加载中..."
        expensePercent.value = "加载中..."
        expensePercentHint.value = R.string.brvah_loading
    }

    override fun onBillLoaded(bill: Bill?) {
        this.bill.value = bill ?: return
        val month = bill.date.monthOfYear
        val year = bill.date.year
        // 默认为月度统计
        val start = DateTime(year, month, 1, 0, 0)
        val end = start.plusMonths(1)
        update(bill, start, end)
    }

    private fun update(bill: Bill?, start: DateTime, end: DateTime) {
        bill ?: return
        mRepository.getAccount(bill.accountId, object : LoadAccountCallBack {
            override fun onAccountLoaded(account: Account) {
                accountName.value = account.name
            }
        })
        mRepository.getType(bill.typeId, object : LoadTypeCallBack {
            override fun onTypeLoaded(type: Type?) {
                type?.let { t ->
                    typeImage.value = t.assetsName
                    typeName.value = t.name
                    balanceColor.value = if (t.isExpense) R.color.deeporange800 else R.color.lightgreen700
                    expensePercentHint.value = if (t.isExpense) R.string.expense_percent else R.string.income_percent
                    mRepository.getAccountStats(bill.accountId, start, end, object : LoadAccountStatsCallBack {
                        override fun onAccountStatsLoaded(accountStats: AccountStats?) {
                            accountStats?.let {
                                accountPercent.value = if (t.isExpense) getPercent(bill, it.expense) else getPercent(bill, it.income)
                            }
                        }
                    })
                    mRepository.getBillStats(start, end, object : LoadBillStatsCallBack {
                        override fun onBillStatsLoaded(billStats: BillStats?) {
                            billStats?.let {
                                expensePercent.value = if (t.isExpense) getPercent(bill, it.expense) else getPercent(bill, it.income)
                            }
                        }
                    })
                }
            }
        })
        mRepository.getTypeStats(start, end, bill.typeId, object : LoadTypeStatsCallBack {
            override fun onTypeStatsLoaded(typeStats: TypeStats?) {
                typeStats?.let {
                    typePercent.value = getPercent(bill, it.balance)
                }
            }
        })
        mRepository.getTypeAverage(start, end, bill.typeId, object : LoadTypeStatsCallBack {
            override fun onTypeStatsLoaded(typeStats: TypeStats?) {
                typeStats?.let {
                    val sub = bill.balance?.subtract(it.balance)?.abs()
                    try {
                        thanAverage.value = sub?.multiply(BigDecimal.valueOf(100))
                                ?.divide(it.balance, 2, BigDecimal.ROUND_HALF_UP).toString() + "%"
                    } catch (e: ArithmeticException) {
                        thanAverage.value = "ERROR"
                    }
                    typeAverage.value = FormatUtil.getNumeric(it.balance)
                    thanAverageHint.value = if (bill.balance ?: return >= it.balance) R.string.higher_than_average else R.string.less_than_average
                }
            }
        })
        balance.value = FormatUtil.getNumeric(bill.balance)
        time.value = bill.date.toString("yyyy-MM-dd hh:mm")
        remark.value = bill.remark
    }

    /**
     * 选择日期区间 0：月 1：年 2：日
     */
    fun setDate() {
        setLoading()
        val start: DateTime
        val end: DateTime
        val day = bill.value?.date?.dayOfMonth ?: return
        val month = bill.value?.date?.monthOfYear ?: return
        val year = bill.value?.date?.year ?: return
        mode++
        mode %= 3
        when (mode) {
            0 -> {
                start = DateTime(year, month, 1, 0, 0)
                end = start.plusMonths(1)
                modeText.value = R.string.monthly_stats
                update(bill.value, start, end)
            }
            1 -> {
                start = DateTime(year, 1, 1, 0, 0)
                end = start.plusYears(1)
                modeText.value = R.string.annual_stats
                update(bill.value, start, end)
            }
            2 -> {
                start = DateTime(year, month, day, 0, 0)
                end = start.plusDays(1)
                modeText.value = R.string.day_stats
                update(bill.value, start, end)
            }
            else -> {
            }
        }
    }

    /**
     * 当前账单所占百分数
     *
     * @return 百分数，带百分号
     */
    private fun getPercent(bill: Bill, divided: BigDecimal): String {
        var bigDecimal: BigDecimal = divided
        var balance = bill.balance ?: return ""
        // 确保balance小于bigDecimal
        if (balance > bigDecimal) {
            val t = balance
            balance = bigDecimal
            bigDecimal = t
        }
        return try {
            balance.multiply(BigDecimal.valueOf(100))
                    .divide(bigDecimal, 2, BigDecimal.ROUND_HALF_UP).toString() + "%"
        } catch (e: ArithmeticException) {
            "ERROR"
        }
    }

    fun delBill(billId: UUID) {
        mRepository.delBill(billId)
    }
}