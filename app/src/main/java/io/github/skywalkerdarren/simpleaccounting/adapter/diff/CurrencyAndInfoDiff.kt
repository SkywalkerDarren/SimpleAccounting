package io.github.skywalkerdarren.simpleaccounting.adapter.diff

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo

class CurrencyAndInfoDiff(newList: List<CurrencyAndInfo>?) : BaseQuickDiffCallback<CurrencyAndInfo>(newList) {
    override fun areItemsTheSame(oldItem: CurrencyAndInfo, newItem: CurrencyAndInfo) = oldItem == newItem

    override fun areContentsTheSame(oldItem: CurrencyAndInfo, newItem: CurrencyAndInfo) =
            oldItem.currency == newItem.currency && oldItem.currencyInfo == newItem.currencyInfo

}