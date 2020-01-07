package io.github.skywalkerdarren.simpleaccounting.model

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.DateHeaderDivider
import io.github.skywalkerdarren.simpleaccounting.model.dao.*
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase.Companion.getInstance
import io.github.skywalkerdarren.simpleaccounting.model.datasource.AccountDataSource.LoadAccountCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.AccountDataSource.LoadAccountsCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.AppDataSource
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource.*
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource.*
import io.github.skywalkerdarren.simpleaccounting.model.datasource.StatsDataSource.*
import io.github.skywalkerdarren.simpleaccounting.model.datasource.TypeDataSource.LoadTypeCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.TypeDataSource.LoadTypesCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.*
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors
import io.github.skywalkerdarren.simpleaccounting.util.data.JsonConvertor
import io.github.skywalkerdarren.simpleaccounting.util.data.PreferenceUtil
import io.github.skywalkerdarren.simpleaccounting.util.network.RequestService
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.collections.ArrayList
import kotlin.concurrent.read
import kotlin.concurrent.withLock
import kotlin.concurrent.write

class AppRepository private constructor(val executors: AppExecutors, val database: AppDatabase) : AppDataSource {
    private val dbLock = ReentrantReadWriteLock(true)
    private val accountDao: AccountDao = database.accountDao()
    private val typeDao: TypeDao = database.typeDao()
    private val billDao: BillDao = database.billDao()
    private val statsDao: StatsDao = database.statsDao()
    private val currencyInfoDao: CurrencyInfoDao = database.currencyInfoDao()
    private val currencyRateDao: CurrencyRateDao = database.currencyRateDao()

    private constructor(executors: AppExecutors, context: Context) : this(
            executors = executors,
            database = getInstance(context))


    private fun slowDown() {
        if (DEBUG) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }


    fun getAccountsOnBackground(callBack: LoadAccountsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getAccountsOnBackground: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val accounts = accountDao.accounts
                dbLock.readLock().unlock()
                callBack.onAccountsLoaded(accounts)
            }
        })
    }

    override fun getBillInfoList(year: Int, month: Int, callBack: LoadBillsInfoCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getBillInfoList: in " + Thread.currentThread().name)
                val start = DateTime(year, month, 1, 1, 0, 0)
                val end = start.plusMonths(1)

                dbLock.readLock().lock()
                val bills = billDao.getsBillsByDate(start, end)
                val billInfoList: MutableList<BillInfo> = ArrayList()
                var date: DateTime? = null

                // 上一个账单的年月日
                bills?.indices?.forEach { i ->
                    val bill = bills[i]
                    val type = typeDao.getType(bill.typeId)
                    val dateTime = bill.date
                    val y = dateTime.year
                    val m = dateTime.monthOfYear
                    val d = dateTime.dayOfMonth
                    // 当前账单的年月日
                    val currentDate = DateTime(y, m, d, 0, 0)
                    // 如果当前帐单与上一张单年月日不同，则添加账单
                    if (date == null || date != currentDate) {
                        date = currentDate
                        val income = getBillStats(currentDate, currentDate.plusDays(1)).income
                        val expense = getBillStats(currentDate, currentDate.plusDays(1)).expense
                        billInfoList.add(BillInfo(DateHeaderDivider(currentDate, income, expense)))
                    }
                    Log.d(TAG, "getBillInfoList: $bill")
                    if (type != null) billInfoList.add(BillInfo(bill, type))
                }
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onBillsInfoLoaded(billInfoList) }
            }
        })
    }

    override fun getAccount(uuid: UUID, callBack: LoadAccountCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getAccount: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val account = accountDao.getAccount(uuid)
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onAccountLoaded(account) }
            }
        })
    }

    override fun getsBills(year: Int, month: Int, callBack: LoadBillsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getsBills: in " + Thread.currentThread().name)
                val start = DateTime(year, month, 1, 1, 0, 0)
                val end = start.plusMonths(1)
                dbLock.readLock().lock()
                val bills = billDao.getsBillsByDate(start, end)
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onBillsLoaded(bills) }
            }
        })
    }

    override fun getAccounts(callBack: LoadAccountsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getAccounts: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val accounts = accountDao.accounts
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onAccountsLoaded(accounts) }
            }
        })
    }

    override fun updateAccountBalance(uuid: UUID, balance: BigDecimal) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "updateAccountBalance" + Thread.currentThread().name)
                dbLock.writeLock().withLock {
                    accountDao.updateAccountBalance(uuid, balance)
                }
            }
        })
    }

    override fun delAccount(uuid: UUID) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "delAccount: in " + Thread.currentThread().name)
                dbLock.writeLock().lock()
                accountDao.delAccount(uuid)
                dbLock.writeLock().unlock()
            }
        })
    }

    override fun changePosition(a: Account, b: Account) {
        execute(object : LoadData {
            override fun load() {
                dbLock.writeLock().lock()
                Log.d(TAG, "changePosition: in " + Thread.currentThread().name)
                val i = a.id
                val j = b.id
                a.id = j
                b.id = i
                accountDao.updateAccountId(a.uuid, -1)
                accountDao.updateAccountId(b.uuid, i)
                accountDao.updateAccountId(a.uuid, j)
                dbLock.writeLock().unlock()
            }
        })
    }

    override fun getBillsCount(callBack: LoadBillCountCallBack) {
        execute(object : LoadData {
            override fun load() {
                dbLock.readLock().lock()
                Log.d(TAG, "getBillsCount: in " + Thread.currentThread().name)
                val count = billDao.billsCount
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onBillCountLoaded(count) }
            }
        })
    }

    override fun getBillsCount(year: Int, month: Int, callBack: LoadBillCountCallBack) {
        execute(object : LoadData {
            override fun load() {
                dbLock.readLock().lock()
                Log.d(TAG, "getBillsCount: in " + Thread.currentThread().name)
                val start = DateTime(year, month, 1, 0, 0)
                val end = start.plusMonths(1)
                val count = billDao.getBillsCount(start, end)
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onBillCountLoaded(count) }
            }
        })
    }

    override fun getBill(id: UUID, callBack: LoadBillCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getBill: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                //Bill bill = sBillCache.get(id);
                //if (bill == null) {
                //    bill = mBillDao.getBill(id);
                //    try {
                //        sBillCache.put(id, bill);
                //    } catch (NullPointerException ignore) {
                //
                //    }
                //}
                val bill = billDao.getBill(id)
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onBillLoaded(bill) }
            }
        })
    }

    override fun addBill(bill: Bill) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "addBill: in " + Thread.currentThread().name)
                dbLock.writeLock().lock()
                updateAccountBalanceByAdd(bill)
                billDao.addBill(bill)
                //sBillCache.put(bill.getUuid(), bill);
                dbLock.writeLock().unlock()
            }
        })
    }

    override fun delBill(id: UUID) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "delBill: in " + Thread.currentThread().name)
                val bill = billDao.getBill(id) ?: return
                dbLock.writeLock().lock()
                updateAccountBalanceByMinus(bill)
                billDao.delBill(id)
                dbLock.writeLock().unlock()
            }
        })
    }

    private fun updateAccountBalanceByAdd(bill: Bill) {
        val type = typeDao.getType(bill.typeId) ?: return
        val account = accountDao.getAccount(bill.accountId)
        val balance = account.balance.add(if (type.isExpense) bill.balance!!.negate() else bill.balance)
        accountDao.updateAccountBalance(account.uuid, balance)
    }

    private fun updateAccountBalanceByMinus(bill: Bill) {
        val account = accountDao.getAccount(bill.accountId)
        val balance = account.balance.add(bill.balance)
        accountDao.updateAccountBalance(account.uuid, balance)
    }

    override fun updateBill(bill: Bill) {
        execute(object : LoadData {
            override fun load() {
                dbLock.write {
                    Log.d(TAG, "updateBill: in " + Thread.currentThread().name + bill)
                    val old = billDao.getBill(bill.uuid) ?: return
                    updateAccountBalanceByMinus(old)
                    updateAccountBalanceByAdd(bill)
                    billDao.updateBill(bill)
                }
            }
        })
    }

    override fun clearBill() {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "clearBill: in " + Thread.currentThread().name)
                dbLock.writeLock().lock()
                val accounts = accountDao.accounts
                accounts.forEach { accountDao.updateAccountBalance(it.uuid, BigDecimal.ZERO) }
                billDao.clearBill()
                dbLock.writeLock().unlock()
            }
        })
    }

    override fun getType(uuid: UUID, callBack: LoadTypeCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getType: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val type = typeDao.getType(uuid)
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onTypeLoaded(type) }
            }
        })
    }

    override fun getTypes(isExpense: Boolean, callBack: LoadTypesCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getTypes: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val types = typeDao.getTypes(isExpense)

                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onTypesLoaded(types) }
            }
        })
    }

    fun getTypesOnBackground(isExpense: Boolean, callBack: LoadTypesCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getTypesOnBackground: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val types = typeDao.getTypes(isExpense)
                dbLock.readLock().unlock()
                callBack.onTypesLoaded(types)
            }
        })
    }

    override fun delType(uuid: UUID) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "delType: in " + Thread.currentThread().name)
                dbLock.writeLock().lock()
                typeDao.delType(uuid)
                dbLock.writeLock().unlock()
            }
        })
    }

    private fun getBillStats(start: DateTime, end: DateTime): BillStats {
        dbLock.readLock().lock()
        val stats = statsDao.getBillsStats(start, end)
        dbLock.readLock().unlock()
        val billStats = BillStats()
        if (stats != null) {
            for ((balance, expense) in stats) {
                if (expense) {
                    billStats.expense = balance
                } else {
                    billStats.income = balance
                }
            }
        }
        return billStats
    }

    override fun getBillsAnnualStats(year: Int, callBack: LoadBillsStatsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getBillsAnnualStats: in " + Thread.currentThread().name)
                var start = DateTime(year, 1, 1, 0, 0, 0)
                var end = start.plusMonths(1)
                val billStatsList: MutableList<BillStats> = ArrayList(12)
                dbLock.readLock().lock()
                for (i in 1..12) {
                    val stats = statsDao.getBillsStats(start, end)
                    val billStats = BillStats()
                    if (stats != null) {
                        for ((balance, expense) in stats) {
                            if (expense) {
                                billStats.expense = balance
                            } else {
                                billStats.income = balance
                            }
                        }
                    }
                    billStatsList.add(billStats)
                    start = start.plusMonths(1)
                    end = end.plusMonths(1)
                }
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onBillStatsLoaded(billStatsList) }
            }
        })
    }

    override fun getBillStats(start: DateTime, end: DateTime, callBack: LoadBillStatsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getBillStats: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val stats = statsDao.getBillsStats(start, end)
                dbLock.readLock().unlock()
                val billStats = BillStats()
                if (stats != null) {
                    for ((balance, expense) in stats) {
                        if (expense) {
                            billStats.expense = balance
                        } else {
                            billStats.income = balance
                        }
                    }
                }
                executors.mainThread().execute { callBack.onBillStatsLoaded(billStats) }
            }
        })
    }

    override fun getTypesStats(start: DateTime, end: DateTime, isExpense: Boolean, callBack: LoadTypesStatsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getTypesStats: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val typesStats = statsDao.getTypesStats(start, end, isExpense)
                executors.mainThread().execute { callBack.onTypesStatsLoaded(typesStats) }
                dbLock.readLock().unlock()
            }
        })
    }

    override fun getTypeStats(start: DateTime, end: DateTime, typeId: UUID, callBack: LoadTypeStatsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getTypeStats: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val typeStats = statsDao.getTypeStats(start, end, typeId)
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onTypeStatsLoaded(typeStats) }
            }
        })
    }

    override fun getTypeAverage(start: DateTime, end: DateTime, typeId: UUID, callBack: LoadTypeStatsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getTypeAverage: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val typeStats = statsDao.getTypeAverageStats(start, end, typeId)
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onTypeStatsLoaded(typeStats) }
            }
        })
    }

    override fun getAccountStats(accountId: UUID, start: DateTime, end: DateTime, callBack: LoadAccountStatsCallBack) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getAccountStats: in " + Thread.currentThread().name)
                dbLock.readLock().lock()
                val accountStats = AccountStats()
                statsDao.getAccountStats(accountId, start, end)
                        ?.forEach { (balance, expense) ->
                            if (expense) {
                                accountStats.expense = balance
                            } else {
                                accountStats.income = balance
                            }
                        }
                dbLock.readLock().unlock()
                executors.mainThread().execute { callBack.onAccountStatsLoaded(accountStats) }
            }
        })
    }

    override fun changeCurrencyPosition(currencyA: Currency, currencyB: Currency) {
        execute(object : LoadData {
            override fun load() {
                dbLock.writeLock().lock()
                Log.d(TAG, "changeCurrencyPosition: in " + Thread.currentThread().name)
                val i = currencyA.id
                val j = currencyB.id
                currencyA.id = j
                currencyB.id = i
                currencyRateDao.updateCurrencyId(currencyA.name, -1)
                currencyRateDao.updateCurrencyId(currencyB.name, i)
                currencyRateDao.updateCurrencyId(currencyA.name, j)
                dbLock.writeLock().unlock()
            }
        })
    }

    override fun setCurrencyFav(name: String, isChecked: Boolean) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "setCurrencyFav: in " + Thread.currentThread().name)
                dbLock.write {
                    currencyRateDao.getCurrency(name).apply {
                        favourite = isChecked
                    }.let {
                        currencyRateDao.updateCurrency(it)
                    }
                }
            }
        })
    }

    override fun getAllCurrencies(callback: LoadPairCurrenicesCallback) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getAllCurrencies: in " + Thread.currentThread().name)
                val pairs: MutableList<Pair<Currency, CurrencyInfo>> = ArrayList()
                dbLock.read {
                    val temp: MutableList<Pair<Currency, CurrencyInfo>> = ArrayList()
                    currencyInfoDao.infos?.forEach { info ->
                        val currency: Currency = currencyRateDao.getCurrency(info.name)

                        if (info.fullNameCN == null) {
                            temp.add(Pair(currency, info))
                        } else {
                            pairs.add(Pair(currency, info))
                        }
                    }
                    pairs.addAll(temp)
                }
                executors.mainThread().execute { callback.onPairCurrenicesLoaded(pairs.toList()) }
            }
        })
    }

    override fun getCurrencyInfos(callback: LoadCurrenciesInfoCallback) {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "getCurrencyInfos: in " + Thread.currentThread().name)
                var infos: List<CurrencyInfo>? = null
                dbLock.read {
                    val t1 = currencyInfoDao.infos?.filter { it.fullNameCN == null } ?: listOf()
                    val t2 = currencyInfoDao.infos?.filter { it.fullNameCN != null } ?: listOf()
                    infos = listOf(t2, t1).flatten()
                }
                if (infos == null) {
                    executors.mainThread().execute { callback.onDataUnavailable() }
                } else {
                    executors.mainThread().execute { callback.onCurrenciesInfoLoaded(infos) }
                }
            }
        })
    }

    override fun updateCurrencies(context: Context, callback: UpdateCallback) {
        val timestamp = PreferenceUtil.getString(context, PreferenceUtil.LAST_UPDATE_TIMESTAMP, "0")
        val before = DateTime(timestamp.toLong() * 1000)
        val after = DateTime.now()
        if (!after.minusDays(1).isAfter(before)) {
            callback.connectFailed("updated too frequently")
            return
        }

        val request2 = RequestService().requestCurrenciesInfo(context)
        request2.enqueue(object : Callback<CurrenciesInfo> {
            override fun onFailure(call: Call<CurrenciesInfo>, t: Throwable) {
                callback.connectFailed(t.message)
            }

            override fun onResponse(call: Call<CurrenciesInfo>, response: Response<CurrenciesInfo>) {
                val currenciesInfo = response.body()

                if ("false" == currenciesInfo?.success) {
                    callback.connectFailed(currenciesInfo.error.toString())
                    return
                }

                PreferenceUtil.setString(context, PreferenceUtil.LAST_UPDATE_TIMESTAMP, currenciesInfo?.timestamp)
                val currencies: List<Currency>? = currenciesInfo?.quotes
                if (currencies == null) {
                    callback.connectFailed("no currency")
                    return
                }
                execute(object : LoadData {
                    override fun load() {
                        for (currency in currencies) {
                            val rawCurrency = currencyRateDao.getCurrency(currency.name)
                            rawCurrency.exchangeRate = currency.exchangeRate
                            currencyRateDao.updateCurrency(rawCurrency)
                        }
                        executors.mainThread().execute { callback.updated() }
                    }
                })

            }
        })
    }

    override fun getCurrencyInfo(name: String, callback: LoadCurrencyInfoCallback) {
        execute(object : LoadData {
            override fun load() {
                val info = currencyInfoDao.getInfo(name)
                if (info != null) {
                    executors.mainThread().execute { callback.onCurrencyInfoLoaded(info) }
                } else {
                    executors.mainThread().execute { callback.onDataUnavailable() }
                }
            }
        })
    }

    override fun getFavouriteCurrenciesInfo(callback: LoadCurrenciesInfoCallback) {
        execute(object : LoadData {
            override fun load() {
                val favouriteCurrencies = currencyRateDao.getFavouriteCurrencies(true)
                val infos: MutableList<CurrencyInfo> = ArrayList()
                if (favouriteCurrencies != null) {
                    for (currency in favouriteCurrencies) {
                        val info = currencyInfoDao.getInfo(currency.name)
                        info?.let { infos.add(it) }
                    }
                }
                if (infos.isEmpty()) {
                    executors.mainThread().execute { callback.onDataUnavailable() }
                } else {
                    executors.mainThread().execute { callback.onCurrenciesInfoLoaded(infos) }
                }
            }
        })
    }

    override fun getFavouriteCurrenciesExchangeRate(from: String, callback: LoadExchangeRatesCallback) {
        execute(object : LoadData {
            override fun load() {
                val currencyFrom = currencyRateDao.getCurrency(from)
                val favouriteCurrencies = currencyRateDao.getFavouriteCurrencies(true)
                if (favouriteCurrencies != null && favouriteCurrencies.isNotEmpty()) {
                    for (currencyTo in favouriteCurrencies) {
                        val rate = currencyTo.exchangeRate / currencyFrom.exchangeRate
                        currencyTo.exchangeRate = rate
                        currencyTo.source = from
                    }
                    executors.mainThread().execute { callback.onExchangeRatesLoaded(favouriteCurrencies) }
                } else {
                    executors.mainThread().execute { callback.onDataUnavailable() }
                }
            }
        })
    }

    override fun getCurrenciesExchangeRate(from: String, callback: LoadExchangeRatesCallback) {
        execute(object : LoadData {
            override fun load() {
                val currencyFrom = currencyRateDao.getCurrency(from)
                val currencies = currencyRateDao.currencies
                if (currencies != null && currencies.isNotEmpty()) {
                    for (currencyTo in currencies) {
                        val rate = currencyTo.exchangeRate / currencyFrom.exchangeRate
                        currencyTo.exchangeRate = rate
                        currencyTo.source = from
                    }
                    executors.mainThread().execute { callback.onExchangeRatesLoaded(currencies) }
                } else {
                    executors.mainThread().execute { callback.onDataUnavailable() }
                }
            }
        })
    }

    override fun getCurrencyExchangeRate(from: String, to: String, callback: LoadExchangeRateCallback) {
        execute(object : LoadData {
            override fun load() {
                val currencyFrom = currencyRateDao.getCurrency(from)
                val currencyTo = currencyRateDao.getCurrency(to)
                val rate = currencyTo.exchangeRate / currencyFrom.exchangeRate
                val currency = Currency(from, to, rate)
                executors.mainThread().execute { callback.onExchangeRateLoaded(currency) }
            }
        })
    }

    override fun getCurrency(name: String, callback: LoadExchangeRateCallback) {
        execute(object : LoadData {
            override fun load() {
                val currency = currencyRateDao.getCurrency(name)
                executors.mainThread().execute { callback.onExchangeRateLoaded(currency) }
            }
        })
    }

    fun initDb() {
        execute(object : LoadData {
            override fun load() {
                Log.d(TAG, "initDb: in " + Thread.currentThread().name)
                dbLock.writeLock().lock()
                accountDao.newAccount(Account("现金", "现金金额",
                        BigDecimal.ZERO, R.color.amber500, "account/cash.png"))
                accountDao.newAccount(Account("支付宝", "在线支付余额",
                        BigDecimal.ZERO, R.color.lightblue500, "account/alipay.png"))
                accountDao.newAccount(Account("微信", "在线支付余额",
                        BigDecimal.ZERO, R.color.lightgreen500, "account/wechat.png"))
                typeDao.newType(Type("吃喝", R.color.diet,
                        true, "type/diet.png"))
                typeDao.newType(Type("娱乐", R.color.entertainment,
                        true, "type/entertainment.png"))
                typeDao.newType(Type("交通", R.color.traffic,
                        true, "type/traffic.png"))
                typeDao.newType(Type("日用品", R.color.daily_necessities,
                        true, "type/daily_necessities.png"))
                typeDao.newType(Type("化妆护肤", R.color.make_up,
                        true, "type/make_up.png"))
                typeDao.newType(Type("医疗", R.color.medical,
                        true, "type/medical.png"))
                typeDao.newType(Type("服饰", R.color.apparel,
                        true, "type/apparel.png"))
                typeDao.newType(Type("话费", R.color.calls,
                        true, "type/calls.png"))
                typeDao.newType(Type("红包", R.color.red_package,
                        true, "type/red_package.png"))
                typeDao.newType(Type("其他", R.color.other,
                        true, "type/other.png"))
                typeDao.newType(Type("工资", R.color.wage,
                        false, "type/wage.png"))
                typeDao.newType(Type("兼职", R.color.part_time,
                        false, "type/part_time.png"))
                typeDao.newType(Type("奖金", R.color.prize,
                        false, "type/prize.png"))
                typeDao.newType(Type("理财投资", R.color.invest,
                        false, "type/invest.png"))
                typeDao.newType(Type("红包", R.color.red_package,
                        false, "type/red_package.png"))
                typeDao.newType(Type("其他", R.color.other,
                        false, "type/other.png"))
                dbLock.writeLock().unlock()
            }
        })
    }

    override fun initCurrenciesAndInfos(context: Context) {
        execute(object : LoadData {
            override fun load() {
                try {
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
                                currencyRateDao.addCurrency(currency)
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                updateCurrencies(context, object : UpdateCallback {
                    override fun connectFailed(msg: String?) {
                        Log.e(TAG, "connectFailed: $msg")
                    }

                    override fun updated() {
                        Log.d(TAG, "updated: ")
                    }
                })

                try {
                    val nameIs = context.resources.assets.open("currency/name.json")
                    val translationCnIs = context.resources.assets.open("currency/translation_cn.json")
                    val flagPath = "currency/flag"
                    val flags = context.resources.assets.list(flagPath)
                    val nameReader: Reader = InputStreamReader(nameIs)
                    val translationCnReader: Reader = InputStreamReader(translationCnIs)
                    val codeMap = JsonConvertor.toCurrencyCodeMap(nameReader)
                    val translationCnCodeMap = JsonConvertor.toCurrencyCodeMap(translationCnReader)
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
                        currencyInfoDao.addInfo(info)
                    }
                    nameIs.close()
                    translationCnIs.close()
                    nameReader.close()
                    translationCnReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun execute(loadData: LoadData) {
        val runnable = Runnable {
            slowDown()
            loadData.load()
        }
        executors.diskIO().execute(runnable)
    }

    private interface LoadData {
        fun load()
    }

    companion object {
        private const val TAG = "AppRepository"
        private const val DEBUG = false
        @Volatile
        private var INSTANCE: AppRepository? = null

        @JvmStatic
        fun getInstance(executors: AppExecutors, context: Context): AppRepository? =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: AppRepository(executors, context).also { INSTANCE = it }
                }

        @JvmStatic
        @VisibleForTesting
        fun getInstance(executors: AppExecutors, database: AppDatabase): AppRepository? =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: AppRepository(executors, database).also { INSTANCE = it }
                }

        @JvmStatic
        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}