package io.github.skywalkerdarren.simpleaccounting.model.datasource

import android.content.Context
import androidx.lifecycle.LiveData
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

interface CurrencyDataSource {
    suspend fun updateCurrencies(newData: List<Currency>)
    suspend fun initCurrenciesAndInfos(context: Context)
    suspend fun changeCurrencyPosition(currencyA: Currency, currencyB: Currency)
    suspend fun setCurrencyFav(currency: Currency, isChecked: Boolean)

    fun getCurrency(name: String): LiveData<Currency>
    fun getCurrencyExchangeRate(from: String, to: String): LiveData<Currency>
    fun getCurrenciesExchangeRate(from: String): LiveData<List<Currency>>
    fun getFavouriteCurrenciesExchangeRate(from: String): LiveData<List<CurrencyAndInfo>>
    fun getCurrencyInfo(name: String): LiveData<CurrencyInfo>
    val favouriteCurrenciesInfo: LiveData<List<CurrencyInfo>>
    val allCurrencies: LiveData<List<CurrencyAndInfo>>
    val currencyInfos: LiveData<List<CurrencyInfo>>
}