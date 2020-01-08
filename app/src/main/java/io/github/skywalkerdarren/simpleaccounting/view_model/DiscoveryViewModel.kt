package io.github.skywalkerdarren.simpleaccounting.view_model

import android.content.Context
import androidx.lifecycle.*
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource.LoadBillCountCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class DiscoveryViewModel(private val mRepository: AppRepository, private val currencyRepo: CurrencyRepo) : ViewModel() {
    val defCurrency: MutableLiveData<String> = MutableLiveData("CNY")
    val cumulativeDays = MutableLiveData<String>()
    val monthOfAccountingCounts = MutableLiveData<String>()
    val sumOfAccountingCounts = MutableLiveData<String>()
    val monthReport = MutableLiveData<String>()
    val yearReport = MutableLiveData<String>()
    val weekReport = MutableLiveData<String>()
    val currentCurrency: LiveData<CurrencyInfo> = Transformations.switchMap(defCurrency) {
        currencyRepo.getCurrencyInfo(it)
    }
    val favoriteCurrencies: LiveData<List<CurrencyAndInfo>> = Transformations.switchMap(defCurrency) {
        currencyRepo.getFavouriteCurrenciesExchangeRate(it)
    }

    val currencyDate = MutableLiveData<String>()

    fun setCurrencyDate(date: String) {
        currencyDate.value = date
    }

    fun setCumulativeDays(days: String) {
        cumulativeDays.value = days
    }

    fun refreshCurrency(context: Context) {
//        mRepository.updateCurrencies(context, callback)
    }

    fun start() {
        mRepository.getBillsCount(object : LoadBillCountCallBack {
            override fun onBillCountLoaded(count: Int) {
                sumOfAccountingCounts.value = count.toString() + "单"
            }
        })
        val dateTime = DateTime()
        mRepository.getBillsCount(dateTime.year, dateTime.monthOfYear,
                object : LoadBillCountCallBack {
                    override fun onBillCountLoaded(count: Int) {
                        monthOfAccountingCounts.value = count.toString() + "单"
                    }
                })
        monthReport.value = dateTime.monthOfYear.toString() + "月报表"
        yearReport.value = dateTime.year.toString() + "年报表"
        weekReport.value = "第" + dateTime.weekOfWeekyear + "周报表"
    }

    fun changeCurrency(currencyA: Currency?, currencyB: Currency?) {
        currencyA ?: return
        currencyB ?: return
        viewModelScope.launch(context = Dispatchers.IO) {
            currencyRepo.changeCurrencyPosition(currencyA, currencyB)
        }
    }
}