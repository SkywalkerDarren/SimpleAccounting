package io.github.skywalkerdarren.simpleaccounting.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemCurrencySingleBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

class CurrencySelectAdapter @JvmOverloads constructor(data: MutableList<CurrencyInfo>? = null) : BaseDataBindingAdapter<CurrencyInfo, ItemCurrencySingleBinding>(R.layout.item_currency_single, data) {
    var current: String? = null

    override fun convert(holder: BaseDataBindingHolder<ItemCurrencySingleBinding>, item: CurrencyInfo) {
        holder.dataBinding?.info = item
        holder.dataBinding?.selected?.isChecked = current != null && item.name == current
//        holder.dataBinding?.addOnClickListener(R.id.item)
//        holder.dataBinding?.addOnClickListener(R.id.selected)
    }
}
