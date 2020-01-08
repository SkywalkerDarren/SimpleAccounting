package io.github.skywalkerdarren.simpleaccounting.adapter

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
    override fun convert(binding: ItemStatsBinding, item: BillStats) {
        binding.stats = item
        binding.monthTextView.text = (mData.indexOf(item) + 1).toString()
    }
}