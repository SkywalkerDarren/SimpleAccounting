package io.github.skywalkerdarren.simpleaccounting.adapter

import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil
import org.joda.time.DateTime
import java.math.BigDecimal

/**
 * 账单分隔符
 *
 * @author darren
 * @date 2018/2/16
 */
class DateHeaderDivider(dateTime: DateTime, income: BigDecimal, expense: BigDecimal) : HeaderDivider {
    /**
     * 获取分割线日期
     *
     * @return 日期
     */
    val date: DateTime
    private val y: Int = dateTime.year
    private val m: Int = dateTime.monthOfYear
    private val d: Int = dateTime.dayOfMonth
    private val mIncome: BigDecimal
    private val mExpense: BigDecimal

    /**
     * @return 获取账单支出名称
     */
    override val expense: String
        get() = FormatUtil.getNumeric(mExpense)

    /**
     * @return 获取账单收入名称
     */
    override val income: String
        get() = FormatUtil.getNumeric(mIncome)

    init {
        date = DateTime(y, m, d, 0, 0)
        mIncome = income
        mExpense = expense
    }
}