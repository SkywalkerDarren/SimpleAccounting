package io.github.skywalkerdarren.simpleaccounting.view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource.LoadBillCountCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo
import io.github.skywalkerdarren.simpleaccounting.util.data.PreferenceUtil
import io.github.skywalkerdarren.simpleaccounting.util.network.RequestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        viewModelScope.launch(Dispatchers.IO) {
            // 上次更新时间戳
            val timestamp = PreferenceUtil.getString(context, PreferenceUtil.LAST_UPDATE_TIMESTAMP, "0")
            val before = DateTime(timestamp.toLong() * 1000)
            val after = DateTime.now()
            if (!after.minusDays(1).isAfter(before)) {
                fail(context, "更新时间间隔至少一天")
                return@launch
            }

            val service = RequestService().requestCurrenciesInfo(context)
            service.enqueue(object : Callback<CurrenciesInfo> {
                override fun onFailure(call: Call<CurrenciesInfo>, t: Throwable) {
                    fail(context, "网络错误")
                    Log.e("wtf", t.message, t)
                }

                override fun onResponse(call: Call<CurrenciesInfo>, response: Response<CurrenciesInfo>) {
                    if (response.isSuccessful) {
                        val currenciesInfo = response.body()
                        if ("false" == currenciesInfo?.success) {
                            fail(context, "网站错误，大概是token错了")
                            Log.e("wtf", currenciesInfo.error.toString())
                            return
                        }
                        val time = currenciesInfo?.timestamp
                        PreferenceUtil.setString(context, PreferenceUtil.LAST_UPDATE_TIMESTAMP, time)
                        val currencies: List<Currency>? = currenciesInfo?.quotes
                        if (currencies == null || time == null) {
                            fail(context, "没有数据，蜜汁问题")
                            Log.e("wtf", "no currency")
                            return
                        }
                        viewModelScope.launch(Dispatchers.IO) end@{
                            currencyRepo.updateCurrencies(currencies)
                            viewModelScope.launch {
                                currencyDate.value = DateTime(time.toLong() * 1000)
                                        .toString("yyyy-MM-dd")
                            }
                        }
                    }
                }

            })
        }

    }

    private fun fail(context: Context, msg: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
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
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepo.changeCurrencyPosition(currencyA, currencyB)
        }
    }
}