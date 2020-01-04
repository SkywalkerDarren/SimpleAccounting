package io.github.skywalkerdarren.simpleaccounting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource.LoadPairCurrenicesCallback
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

class CurrencyFavViewModel(val repository: AppRepository) : ViewModel() {
    private val currencies = MutableLiveData<List<Pair<Currency, CurrencyInfo>>>()

    fun start() = repository.getAllCurrencies(object : LoadPairCurrenicesCallback {
        override fun onPairCurrenicesLoaded(currency: List<Pair<Currency, CurrencyInfo>>) {
            currencies.value = currency
        }

        override fun onDataUnavailable() {}
    })


    fun getCurrencies(): LiveData<List<Pair<Currency, CurrencyInfo>>> {
        return currencies
    }
}