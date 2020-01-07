package io.github.skywalkerdarren.simpleaccounting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo

class CurrencyFavViewModel(repository: CurrencyRepo) : ViewModel() {
    private val currencies = repository.allCurrencies

    fun getCurrencies(): LiveData<List<CurrencyAndInfo>> {
        return currencies
    }
}