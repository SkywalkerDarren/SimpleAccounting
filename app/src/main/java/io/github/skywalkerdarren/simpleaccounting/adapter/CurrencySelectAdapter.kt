package io.github.skywalkerdarren.simpleaccounting.adapter

import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseBindingViewHolder
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemCurrencySingleBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

class CurrencySelectAdapter @JvmOverloads constructor(data: MutableList<CurrencyInfo>? = null) : BaseDataBindingAdapter<CurrencyInfo, ItemCurrencySingleBinding>(R.layout.item_currency_single, data) {
    var current: String? = null

    override fun convert(binding: ItemCurrencySingleBinding, item: CurrencyInfo) {
        binding.info = item
        binding.selected.isChecked = current != null && item.name == current
    }

    override fun convert(helper: BaseBindingViewHolder<ItemCurrencySingleBinding>, item: CurrencyInfo) {
        super.convert(helper, item)
        helper.addOnClickListener(R.id.item)
        helper.addOnClickListener(R.id.selected)
    }
}
