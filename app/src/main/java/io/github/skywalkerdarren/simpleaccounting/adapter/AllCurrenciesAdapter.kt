package io.github.skywalkerdarren.simpleaccounting.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemCurrencyMultiBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.view_model.CurrencyFavViewModel

/**
 * @author darren
 * @date 2018/4/13
 */
class AllCurrenciesAdapter(private val viewModel: CurrencyFavViewModel) :
        BaseDataBindingAdapter<CurrencyAndInfo, ItemCurrencyMultiBinding>
        (R.layout.item_currency_multi, null) {
    override fun convert(holder: BaseDataBindingHolder<ItemCurrencyMultiBinding>, item: CurrencyAndInfo) {
        holder.dataBinding?.currency = item.currency
        holder.dataBinding?.info = item.currencyInfo
        holder.dataBinding?.favCurrency?.setOnClickListener {
            viewModel.setCurrencyFav(item.currency, holder.dataBinding?.favCurrency?.isChecked
                    ?: false)
        }
    }
}