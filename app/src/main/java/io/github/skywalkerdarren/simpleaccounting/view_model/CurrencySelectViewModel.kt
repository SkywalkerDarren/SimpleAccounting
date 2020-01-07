package io.github.skywalkerdarren.simpleaccounting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo

class CurrencySelectViewModel(repository: CurrencyRepo) : ViewModel() {
    private val currencies: LiveData<List<CurrencyInfo>> = repository.favouriteCurrenciesInfo


    fun getCurrencies(): LiveData<List<CurrencyInfo>> {
        return currencies
    }

}
