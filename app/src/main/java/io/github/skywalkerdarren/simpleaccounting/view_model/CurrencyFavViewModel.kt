package io.github.skywalkerdarren.simpleaccounting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyFavViewModel(private val repository: CurrencyRepo) : ViewModel() {
    private val currencies = repository.allCurrencies

    fun getCurrencies(): LiveData<List<CurrencyAndInfo>> {
        return currencies
    }

    fun setCurrencyFav(currency: Currency?, checked: Boolean) {
        if (currency == null) return
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.setCurrencyFav(currency, checked)
        }
    }
}