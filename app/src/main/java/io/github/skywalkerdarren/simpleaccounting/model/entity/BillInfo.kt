package io.github.skywalkerdarren.simpleaccounting.model.entity

import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter
import io.github.skywalkerdarren.simpleaccounting.adapter.DateHeaderDivider
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil
import org.joda.time.DateTime
import java.util.*

/**
 * 账单摘要信息类
 * 用于构造适用于适配器的账单列表
 *
 * @author darren
 * @date 2018/3/17
 */
data class BillInfo @JvmOverloads constructor(
        val type: Int,

        val uuid: UUID? = null,
        val title: String? = null,
        val remark: String? = null,
        val balance: String? = null,
        val isExpense: Boolean = false,
        val billTypeRes: String? = null,

        val income: String? = null,
        val expense: String? = null,
        val dateTime: DateTime? = null
) : MultiItemEntity {
    /**
     * 构造账单摘要
     *
     * @param bill 账单
     * @param type 类型
     */
    constructor(bill: Bill, type: Type) : this(
            type = if (TextUtils.isEmpty(bill.remark)) BillAdapter.WITHOUT_REMARK else BillAdapter.WITH_REMARK,
            uuid = bill.uuid,
            title = bill.name,
            remark = bill.remark,
            balance = FormatUtil.getNumeric(bill.balance),
            isExpense = type.isExpense,
            billTypeRes = type.assetsName,
            dateTime = bill.date
    )

    /**
     * 构造账单分隔符
     *
     * @param header 分隔符
     */
    constructor(header: DateHeaderDivider) : this(
            type = BillAdapter.HEADER,
            dateTime = header.date,
            expense = header.expense,
            income = header.income
    )

    override fun getItemType(): Int {
        return type
    }
}