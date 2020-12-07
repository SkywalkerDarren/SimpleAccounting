package io.github.skywalkerdarren.simpleaccounting.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemStatsBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats

/**
 * 统计列表转换到recycler view的适配器
 *
 * @author darren
 * @date 2018/3/14
 */
class StatsAdapter : BaseDataBindingAdapter<BillStats, ItemStatsBinding>(R.layout.item_stats, null) {
    override fun convert(holder: BaseDataBindingHolder<ItemStatsBinding>, item: BillStats) {
        holder.dataBinding?.stats = item
        holder.dataBinding?.monthTextView?.text = (data.indexOf(item) + 1).toString()
    }
}