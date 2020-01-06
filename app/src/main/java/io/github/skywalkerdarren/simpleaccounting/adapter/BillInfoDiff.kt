package io.github.skywalkerdarren.simpleaccounting.adapter

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo

class BillInfoDiff(newList: MutableList<BillInfo>?) : BaseQuickDiffCallback<BillInfo>(newList) {

    override fun areItemsTheSame(oldItem: BillInfo, newItem: BillInfo): Boolean {
        if (oldItem.itemType != newItem.itemType) return false
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BillInfo, newItem: BillInfo) = false
}