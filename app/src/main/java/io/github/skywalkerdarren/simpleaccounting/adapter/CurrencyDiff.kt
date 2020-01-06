package io.github.skywalkerdarren.simpleaccounting.adapter

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency

class CurrencyDiff(newList: MutableList<Currency>?) : BaseQuickDiffCallback<Currency>(newList) {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem
}