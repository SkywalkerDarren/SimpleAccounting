package io.github.skywalkerdarren.simpleaccounting.adapter.diff

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeAndStats

class TypeAndStatsDiff(newList: List<TypeAndStats>?) : BaseQuickDiffCallback<TypeAndStats>(newList) {
    override fun areItemsTheSame(oldItem: TypeAndStats, newItem: TypeAndStats) = oldItem == newItem

    override fun areContentsTheSame(oldItem: TypeAndStats, newItem: TypeAndStats) = false

}