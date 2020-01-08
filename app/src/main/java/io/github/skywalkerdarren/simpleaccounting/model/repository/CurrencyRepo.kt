package io.github.skywalkerdarren.simpleaccounting.model.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.skywalkerdarren.simpleaccounting.model.dao.CurrencyInfoDao
import io.github.skywalkerdarren.simpleaccounting.model.dao.CurrencyRateDao
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo
import io.github.skywalkerdarren.simpleaccounting.util.data.JsonConvertor
import io.github.skywalkerdarren.simpleaccounting.util.data.PreferenceUtil
import java.io.InputStreamReader
import java.io.Reader
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CurrencyRepo private constructor(
        private val infoDao: CurrencyInfoDao,
        private val rateDao: CurrencyRateDao
) : CurrencyDataSource {
    companion object {
        private const val TAG = "CurrencyRepo"
        // For Singleton instantiation
        @Volatile
        private var instance: CurrencyRepo? = null

        fun getInstance(infoDao: CurrencyInfoDao, rateDao: CurrencyRateDao) =
                instance ?: synchronized(this) {
                    instance ?: CurrencyRepo(infoDao, rateDao).also { instance = it }
                }
    }

    override suspend fun updateCurrencies(newData: List<Currency>) {
        val currenciesRaw = rateDao.getCurrenciesRaw()
        newData.forEach { newCur ->
            val raw = currenciesRaw.find { newCur.name == it.name && newCur.source == it.source }
            if (raw == null) {
                Log.d("wtf", "not find $newCur to $raw then add new one")
                rateDao.addCurrency(newCur)
            } else {
                raw.exchangeRate = newCur.exchangeRate
                rateDao.updateCurrency(raw)
            }
        }
    }

    override suspend fun initCurrenciesAndInfos(context: Context) {
        context.resources.assets.open("currency/default_rate.json").use { inputStream ->
            val reader: Reader = InputStreamReader(inputStream)
            val info = JsonConvertor.toCurrenciesInfo(reader)
            PreferenceUtil.setString(context, PreferenceUtil.LAST_UPDATE_TIMESTAMP, info.timestamp)
            if (info.quotes != null) {
                for (currency in info.quotes) {
                    when (currency.name) {
                        "CNY", "USD", "HKD", "JPY", "MOP", "TWD", "CAD", "EUR", "GBP", "AUD" ->
                            currency.favourite = java.lang.Boolean.TRUE
                        else -> currency.favourite = java.lang.Boolean.FALSE
                    }
                    rateDao.addCurrency(currency)
                }
            }
        }

        val flagPath = "currency/flag"
        val flags = context.resources.assets.list(flagPath)
        context.resources.assets.open("currency/name.json").reader().use { name ->
            context.resources.assets.open("currency/translation_cn.json").reader().use { translationCn ->
                val codeMap = JsonConvertor.toCurrencyCodeMap(name)
                val translationCnCodeMap = JsonConvertor.toCurrencyCodeMap(translationCn)
                val flagsMap: MutableMap<String, String> = HashMap()
                if (flags != null) {
                    for (s in flags) {
                        val key = s.replace(".png", "")
                        flagsMap[key] = "$flagPath/$s"
                    }
                }
                for (key in codeMap.keys) {
                    val info = CurrencyInfo(
                            key,
                            codeMap[key],
                            translationCnCodeMap[key],
                            flagsMap[key])
                    infoDao.addInfo(info)
                }
            }
        }
    }

    override suspend fun changeCurrencyPosition(currencyA: Currency, currencyB: Currency) {
        rateDao.changeCurrency(currencyA, currencyB)
    }

    override suspend fun setCurrencyFav(currency: Currency, isChecked: Boolean) {
        currency.favourite = isChecked
        rateDao.updateCurrency(currency)
    }

    override fun getCurrency(name: String): LiveData<Currency> = rateDao.getCurrency(name)

    override fun getCurrencyExchangeRate(from: String, to: String): LiveData<Currency> {
        val f = rateDao.getCurrency(from).value ?: return MutableLiveData<Currency>()
        val t = rateDao.getCurrency(to).value?.apply {
            source = f.source
            exchangeRate /= f.exchangeRate
        } ?: return MutableLiveData<Currency>()
        return MutableLiveData<Currency>(t)
    }

    override fun getCurrenciesExchangeRate(from: String): LiveData<List<Currency>> =
            rateDao.currencies

    val i: AtomicInteger = AtomicInteger(0)
    override fun getFavouriteCurrenciesExchangeRate(from: String): LiveData<List<CurrencyAndInfo>> =
            CombineLatestMediatorLiveDataOfTwo(rateDao.getCurrency(from), rateDao.getFavouriteCurrencies(true)) { currency, list ->
                currency
                        ?: return@CombineLatestMediatorLiveDataOfTwo listOf(CurrencyAndInfo(null, null))
                list
                        ?: return@CombineLatestMediatorLiveDataOfTwo listOf(CurrencyAndInfo(null, null))
                val result = list.map {
                    it.currency ?: return@map CurrencyAndInfo(null, null)
                    if (it.currency.source != "USD") return@map it
                    it.currency.source = currency.name
                    it.currency.exchangeRate /= currency.exchangeRate
                    return@map it
                }
                val t1 = result.filter { it.currencyInfo?.fullNameCN == null }
                val t2 = result.filter { it.currencyInfo?.fullNameCN != null }
                return@CombineLatestMediatorLiveDataOfTwo listOf(t2, t1).flatten()
            }

    private class CombineLatestMediatorLiveDataOfTwo<T1, T2, R>(
            source1: LiveData<T1>,
            source2: LiveData<T2>,
            combiner: (T1?, T2?) -> R?
    ) : MediatorLiveData<R>() {
        init {
            addSource(source1) {
                value = combiner(it, source2.value)
            }
            addSource(source2) {
                value = combiner(source1.value, it)
            }
        }
    }

    override fun getCurrencyInfo(name: String): LiveData<CurrencyInfo> = infoDao.getInfo(name)

    override val favouriteCurrenciesInfo: LiveData<List<CurrencyInfo>> = infoDao.favInfos

    override val currencyInfos: LiveData<List<CurrencyInfo>> = Transformations.map(infoDao.infos) { list ->
        val noName = list.filter { it.fullNameCN == null }
        val haveName = list.filter { it.fullNameCN != null }
        return@map listOf(haveName, noName).flatten()
    }

    override val allCurrencies: LiveData<List<CurrencyAndInfo>>
        get() {
            val rate = rateDao.currencies
            val info = infoDao.infos
            return CombineToCurrencyAndInfoList(rate, info) { t1, t2 ->
                t1?.map { currency ->
                    val ci = t2?.filter { currencyInfo -> currencyInfo.name == currency.name }?.get(0)
                    CurrencyAndInfo(currency, ci)
                }
            }
        }

    private class CombineToCurrencyAndInfoList<T1, T2, R>(
            source1: LiveData<T1>,
            source2: LiveData<T2>,
            combiner: (T1?, T2?) -> R?
    ) : MediatorLiveData<R>() {
        init {
            addSource(source1) {
                value = combiner(it, source2.value)
            }
            addSource(source2) {
                value = combiner(source1.value, it)
            }
        }
    }
}