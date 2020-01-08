package io.github.skywalkerdarren.simpleaccounting.adapter.diff

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats

class TypeStatsDiff(newList: List<TypeStats>?) : BaseQuickDiffCallback<TypeStats>(newList) {
    override fun areItemsTheSame(oldItem: TypeStats, newItem: TypeStats) = oldItem == newItem

    override fun areContentsTheSame(oldItem: TypeStats, newItem: TypeStats) = false

}