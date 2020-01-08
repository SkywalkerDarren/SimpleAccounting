package io.github.skywalkerdarren.simpleaccounting.adapter

import android.widget.CompoundButton
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
    override fun convert(binding: ItemCurrencyMultiBinding, item: CurrencyAndInfo) {
        binding.currency = item.currency
        binding.info = item.currencyInfo
        binding.favCurrency.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            viewModel.setCurrencyFav(item.currency?.name, isChecked)
        }
    }
}