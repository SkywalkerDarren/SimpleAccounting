package io.github.skywalkerdarren.simpleaccounting.adapter

import android.app.Application
import android.widget.CompoundButton
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemCurrencyMultiBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author darren
 * @date 2018/4/13
 */
class AllCurrenciesAdapter(application: Application) :
        BaseDataBindingAdapter<CurrencyAndInfo, ItemCurrencyMultiBinding>
        (R.layout.item_currency_multi, null) {
    private val mRepository: CurrencyRepo = ViewModelFactory.getInstance(application).repository
    override fun convert(binding: ItemCurrencyMultiBinding, item: CurrencyAndInfo) {
        binding.currency = item.currency
        binding.info = item.currencyInfo
        binding.favCurrency.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            GlobalScope.launch {

                withContext(Dispatchers.IO) {
                    item.currency?.let { mRepository.setCurrencyFav(it.name, isChecked) }
                }
            }
        }
    }
}