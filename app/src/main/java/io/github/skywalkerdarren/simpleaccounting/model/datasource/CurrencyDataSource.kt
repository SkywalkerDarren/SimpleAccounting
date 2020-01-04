package io.github.skywalkerdarren.simpleaccounting.model.datasource

import android.content.Context
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

interface CurrencyDataSource {
    fun getCurrency(name: String, callback: LoadExchangeRateCallback)
    fun getCurrencyExchangeRate(from: String, to: String, callback: LoadExchangeRateCallback)
    fun getCurrenciesExchangeRate(from: String, callback: LoadExchangeRatesCallback)
    fun getFavouriteCurrenciesExchangeRate(from: String, callback: LoadExchangeRatesCallback)
    fun getCurrencyInfo(name: String, callback: LoadCurrencyInfoCallback)
    fun getFavouriteCurrenciesInfo(callback: LoadCurrenciesInfoCallback)
    fun updateCurrencies(context: Context, callback: UpdateCallback)
    fun initCurrenciesAndInfos(context: Context)
    fun changeCurrencyPosition(currencyA: Currency, currencyB: Currency)
    fun setCurrencyFav(name: String, isChecked: Boolean)
    fun getAllCurrencies(callback: LoadPairCurrenicesCallback)

    interface LoadPairCurrenicesCallback {
        fun onPairCurrenicesLoaded(currency: List<Pair<Currency, CurrencyInfo>>)
        fun onDataUnavailable()
    }

    interface LoadExchangeRateCallback {
        fun onExchangeRateLoaded(currency: Currency?)
        fun onDataUnavailable()
    }

    interface LoadExchangeRatesCallback {
        fun onExchangeRatesLoaded(currencies: List<Currency>?)
        fun onDataUnavailable()
    }

    interface LoadCurrencyInfoCallback {
        fun onCurrencyInfoLoaded(info: CurrencyInfo?)
        fun onDataUnavailable()
    }

    interface LoadCurrenciesInfoCallback {
        fun onCurrenciesInfoLoaded(infos: List<CurrencyInfo>?)
        fun onDataUnavailable()
    }

    interface UpdateCallback {
        fun connectFailed(msg: String?)
        fun updated()
    }
}