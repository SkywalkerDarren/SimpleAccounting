package io.github.skywalkerdarren.simpleaccounting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

class CurrencySelectViewModel(val repository: AppRepository) : ViewModel() {
    private val currencies = MutableLiveData<List<CurrencyInfo>>()

    fun start() = repository.getCurrencyInfos(object : CurrencyDataSource.LoadCurrenciesInfoCallback {
        override fun onCurrenciesInfoLoaded(infos: List<CurrencyInfo>?) {
            currencies.value = infos
        }

        override fun onDataUnavailable() {

        }
    })


    fun getCurrencies(): LiveData<List<CurrencyInfo>> {
        return currencies
    }

}
