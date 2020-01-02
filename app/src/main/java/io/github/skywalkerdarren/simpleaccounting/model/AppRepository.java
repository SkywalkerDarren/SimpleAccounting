package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.DateHeaderDivider;
import io.github.skywalkerdarren.simpleaccounting.model.dao.AccountDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.CurrencyInfoDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.CurrencyRateDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.StatsDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.TypeDao;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.datasource.AppDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Stats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.util.CurrencyRequest;
import io.github.skywalkerdarren.simpleaccounting.util.JsonConvertor;
import io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil;

import static io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil.LAST_UPDATE_TIMESTAMP;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class AppRepository implements AppDataSource {
    private static final String TAG = "AppRepository";
    private static final String ACCOUNTS = "ACCOUNTS";
    private static final String INCOME_TYPES = "INCOME_TYPES";
    private static final String EXPENSE_TYPES = "EXPENSE_TYPES";
    private static final boolean DEBUG = false;
    private static Map<UUID, Account> sAccountCache = new ConcurrentHashMap<>();
    private static Map<String, List<Account>> sAccountsCache = new ConcurrentHashMap<>();
    private static Map<UUID, Bill> sBillCache = new ConcurrentHashMap<>();
    private static Map<UUID, Type> sTypeCache = new ConcurrentHashMap<>();
    private static volatile AppRepository INSTANCE;
    private static Map<String, List<Type>> sTypesCache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock dbLock = new ReentrantReadWriteLock(true);
    private AccountDao mAccountDao;
    private TypeDao mTypeDao;
    private BillDao mBillDao;
    private StatsDao mStatsDao;
    private CurrencyInfoDao mCurrencyInfoDao;
    private CurrencyRateDao mCurrencyRateDao;
    private AppExecutors mExecutors;

    private AppRepository(@NonNull AppExecutors executors, @NonNull Context context) {
        mExecutors = executors;
        AppDatabase database = AppDatabase.getInstance(context);
        initDao(database);
    }

    private AppRepository(@NonNull AppExecutors executors, AppDatabase database) {
        initDao(database);
        mExecutors = executors;
    }

    public static AppRepository getInstance(@NonNull AppExecutors executors, @NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepository(executors, context);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static AppRepository getInstance(@NonNull AppExecutors executors, AppDatabase database) {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepository(executors, database);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
        sTypesCache.clear();
        sAccountsCache.clear();
        sBillCache.clear();
        sTypeCache.clear();
        sAccountCache.clear();
    }

    private void slowDown() {
        if (DEBUG) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDao(AppDatabase database) {
        mAccountDao = database.accountDao();
        mTypeDao = database.typeDao();
        mBillDao = database.billDao();
        mStatsDao = database.statsDao();
        mCurrencyInfoDao = database.currencyInfoDao();
        mCurrencyRateDao = database.currencyRateDao();
    }

    void getAccountsOnBackground(LoadAccountsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccountsOnBackground: in " + currentThread().getName());
            dbLock.readLock().lock();
            List<Account> accounts = sAccountsCache.get(ACCOUNTS);
            if (accounts == null) {
                accounts = mAccountDao.getAccounts();
                sAccountsCache.put(ACCOUNTS, accounts);
            }
            dbLock.readLock().unlock();

            callBack.onAccountsLoaded(accounts);
        });
    }

    @Override
    public void getBillInfoList(int year, int month, LoadBillsInfoCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getBillInfoList: in " + currentThread().getName());
            DateTime start = new DateTime(year, month, 1, 1, 0, 0);
            DateTime end = start.plusMonths(1);

            dbLock.readLock().lock();
            List<Bill> bills = mBillDao.getsBillsByDate(start, end);
            List<BillInfo> billInfoList = new ArrayList<>();
            // 上一个账单的年月日
            DateTime date = null;
            for (int i = 0; i < bills.size(); i++) {
                Bill bill = bills.get(i);
                Type type = mTypeDao.getType(bill.getTypeId());
                DateTime dateTime = bill.getDate();
                int y = dateTime.getYear();
                int m = dateTime.getMonthOfYear();
                int d = dateTime.getDayOfMonth();
                // 当前账单的年月日
                DateTime currentDate = new DateTime(y, m, d, 0, 0);
                // 如果当前帐单与上一张单年月日不同，则添加账单
                if (date == null || !date.equals(currentDate)) {
                    date = currentDate;
                    BigDecimal income = getBillStats(date, date.plusDays(1)).getIncome();
                    BigDecimal expense = getBillStats(date, date.plusDays(1)).getExpense();
                    billInfoList.add(new BillInfo(new DateHeaderDivider(date, income, expense)));
                }
                billInfoList.add(new BillInfo(bill, type));
            }
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onBillsInfoLoaded(billInfoList));
        });

    }

    @Override
    public void getAccount(UUID uuid, LoadAccountCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccount: in " + currentThread().getName());

            dbLock.readLock().lock();
            Account account = sAccountCache.get(uuid);
            if (account == null) {
                account = mAccountDao.getAccount(uuid);
                sAccountCache.put(uuid, account);
            }
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onAccountLoaded(sAccountCache.get(uuid)));
        });
    }

    @Override
    public void getsBills(int year, int month, LoadBillsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getsBills: in " + currentThread().getName());
            DateTime start = new DateTime(year, month, 1, 1, 0, 0);
            DateTime end = start.plusMonths(1);

            dbLock.readLock().lock();
            List<Bill> bills = mBillDao.getsBillsByDate(start, end);
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onBillsLoaded(bills));
        });
    }

    @Override
    public void getAccounts(LoadAccountsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccounts: in " + currentThread().getName());
            dbLock.readLock().lock();
            List<Account> accounts = sAccountsCache.get(ACCOUNTS);
            if (accounts == null) {
                accounts = mAccountDao.getAccounts();
                sAccountsCache.put(ACCOUNTS, accounts);
            }
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onAccountsLoaded(sAccountsCache.get(ACCOUNTS)));
        });
    }

    @Override
    public void delAccount(UUID uuid) {
        execute(() -> {
            Log.d(TAG, "delAccount: in " + currentThread().getName());
            dbLock.writeLock().lock();
            mAccountDao.delAccount(uuid);
            sAccountsCache.clear();
            sAccountCache.remove(uuid);
            dbLock.writeLock().unlock();
        });
    }

    @Override
    public void changePosition(Account a, Account b) {
        execute(() -> {
            dbLock.writeLock().lock();
            Log.d(TAG, "changePosition: in " + currentThread().getName());
            Integer i = a.getId();
            Integer j = b.getId();
            a.setId(j);
            b.setId(i);
            mAccountDao.updateAccountId(a.getUUID(), -1);
            mAccountDao.updateAccountId(b.getUUID(), i);
            mAccountDao.updateAccountId(a.getUUID(), j);
            sAccountCache.put(a.getUUID(), a);
            sAccountCache.put(b.getUUID(), b);
            sAccountsCache.clear();
            dbLock.writeLock().unlock();
        });
    }

    @Override
    public void getBillsCount(LoadBillCountCallBack callBack) {
        execute(() -> {
            dbLock.readLock().lock();
            Log.d(TAG, "getBillsCount: in " + currentThread().getName());
            Integer count = mBillDao.getBillsCount();
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onBillCountLoaded(count));
        });
    }

    @Override
    public void getBillsCount(int year, int month, LoadBillCountCallBack callBack) {
        execute(() -> {
            dbLock.readLock().lock();
            Log.d(TAG, "getBillsCount: in " + currentThread().getName());
            DateTime start = new DateTime(year, month, 1, 0, 0);
            DateTime end = start.plusMonths(1);
            Integer count = mBillDao.getBillsCount();
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onBillCountLoaded(count));
        });
    }

    @Override
    public void getBill(UUID id, LoadBillCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getBill: in " + currentThread().getName());

            dbLock.readLock().lock();
            Bill bill = sBillCache.get(id);
            if (bill == null) {
                bill = mBillDao.getBill(id);
                try {
                    sBillCache.put(id, bill);
                } catch (NullPointerException ignore) {

                }
            }
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onBillLoaded(sBillCache.get(id)));
        });
    }

    @Override
    public void addBill(Bill bill) {
        execute(() -> {
            Log.d(TAG, "addBill: in " + currentThread().getName());
            dbLock.writeLock().lock();
            mBillDao.addBill(bill);
            sBillCache.put(bill.getUUID(), bill);
            dbLock.writeLock().unlock();

        });
    }

    @Override
    public void delBill(UUID id) {
        execute(() -> {
            Log.d(TAG, "delBill: in " + currentThread().getName());
            dbLock.writeLock().lock();
            mBillDao.delBill(id);
            sBillCache.remove(id);
            dbLock.writeLock().unlock();

        });
    }

    @Override
    public void updateBill(Bill bill) {
        execute(() -> {
            Log.d(TAG, "updateBill: in " + currentThread().getName());
            dbLock.writeLock().lock();
            mBillDao.updateBill(bill);
            sBillCache.put(bill.getUUID(), bill);
            dbLock.writeLock().unlock();

        });
    }

    @Override
    public void clearBill() {
        execute(() -> {
            Log.d(TAG, "clearBill: in " + currentThread().getName());
            dbLock.writeLock().lock();
            mBillDao.clearBill();
            sBillCache.clear();
            dbLock.writeLock().unlock();
        });
    }

    @Override
    public void getType(UUID uuid, LoadTypeCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getType: in " + currentThread().getName());
            dbLock.readLock().lock();
            Type type = sTypeCache.get(uuid);
            if (type == null) {
                type = mTypeDao.getType(uuid);
                sTypeCache.put(uuid, type);
            }
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onTypeLoaded(sTypeCache.get(uuid)));
        });
    }

    @Override
    public void getTypes(boolean isExpense, LoadTypesCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypes: in " + currentThread().getName());
            final String flag = isExpense ? EXPENSE_TYPES : INCOME_TYPES;
            dbLock.readLock().lock();
            List<Type> types = sTypesCache.get(flag);
            if (types == null) {
                types = mTypeDao.getTypes(isExpense);
                sTypesCache.put(flag, types);
            }
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onTypesLoaded(sTypesCache.get(flag)));
        });
    }

    void getTypesOnBackground(boolean isExpense, LoadTypesCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypesOnBackground: in " + currentThread().getName());
            final String flag = isExpense ? EXPENSE_TYPES : INCOME_TYPES;
            dbLock.readLock().lock();
            List<Type> types = sTypesCache.get(flag);
            if (types == null) {
                types = mTypeDao.getTypes(isExpense);
                sTypesCache.put(flag, types);
            }
            dbLock.readLock().unlock();

            callBack.onTypesLoaded(types);
        });
    }

    @Override
    public void delType(UUID uuid) {
        execute(() -> {
            Log.d(TAG, "delType: in " + currentThread().getName());
            dbLock.writeLock().lock();
            mTypeDao.delType(uuid);
            sTypeCache.remove(uuid);
            sTypesCache.clear();
            dbLock.writeLock().unlock();
        });
    }

    private BillStats getBillStats(DateTime start, DateTime end) {
        dbLock.readLock().lock();
        List<Stats> stats = mStatsDao.getBillsStats(start, end);
        dbLock.readLock().unlock();

        BillStats billStats = new BillStats();
        for (Stats s : stats) {
            if (s.getExpense()) {
                billStats.setExpense(s.getBalance());
            } else {
                billStats.setIncome(s.getBalance());
            }
        }
        return billStats;
    }

    @Override
    public void getBillsAnnualStats(int year, LoadBillsStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getBillsAnnualStats: in " + currentThread().getName());
            DateTime start = new DateTime(year, 1, 1, 0, 0, 0);
            DateTime end = start.plusMonths(1);
            List<BillStats> billStatsList = new ArrayList<>(12);

            dbLock.readLock().lock();
            for (int i = 1; i <= 12; i++) {
                List<Stats> stats = mStatsDao.getBillsStats(start, end);
                BillStats billStats = new BillStats();
                for (Stats s : stats) {
                    if (s.getExpense()) {
                        billStats.setExpense(s.getBalance());
                    } else {
                        billStats.setIncome(s.getBalance());
                    }
                }
                billStatsList.add(billStats);
                start = start.plusMonths(1);
                end = end.plusMonths(1);
            }
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onBillStatsLoaded(billStatsList));
        });
    }

    @Override
    public void getBillStats(DateTime start, DateTime end, LoadBillStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getBillStats: in " + currentThread().getName());
            dbLock.readLock().lock();
            List<Stats> stats = mStatsDao.getBillsStats(start, end);
            dbLock.readLock().unlock();

            BillStats billStats = new BillStats();
            for (Stats s : stats) {
                if (s.getExpense()) {
                    billStats.setExpense(s.getBalance());
                } else {
                    billStats.setIncome(s.getBalance());
                }
            }
            mExecutors.mainThread().execute(() -> callBack.onBillStatsLoaded(billStats));
        });
    }

    @Override
    public void getTypesStats(DateTime start, DateTime end, boolean isExpense, LoadTypesStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypesStats: in " + currentThread().getName());
            dbLock.readLock().lock();
            List<TypeStats> typesStats = mStatsDao.getTypesStats(start, end, isExpense);
            mExecutors.mainThread().execute(() -> callBack.onTypesStatsLoaded(typesStats));
            dbLock.readLock().unlock();
        });
    }

    @Override
    public void getTypeStats(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypeStats: in " + currentThread().getName());
            dbLock.readLock().lock();
            TypeStats typeStats = mStatsDao.getTypeStats(start, end, typeId);
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onTypeStatsLoaded(typeStats));
        });
    }

    @Override
    public void getTypeAverage(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypeAverage: in " + currentThread().getName());
            dbLock.readLock().lock();
            TypeStats typeStats = mStatsDao.getTypeAverageStats(start, end, typeId);
            dbLock.readLock().unlock();

            mExecutors.mainThread().execute(() -> callBack.onTypeStatsLoaded(typeStats));
        });
    }

    @Override
    public void getAccountStats(UUID accountId, DateTime start, DateTime end, LoadAccountStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccountStats: in " + currentThread().getName());
            dbLock.readLock().lock();
            List<Stats> stats = mStatsDao.getAccountStats(accountId, start, end);
            dbLock.readLock().unlock();

            AccountStats accountStats = new AccountStats();
            for (Stats s : stats) {
                if (s.getExpense()) {
                    accountStats.setExpense(s.getBalance());
                } else {
                    accountStats.setIncome(s.getBalance());
                }
            }
            mExecutors.mainThread().execute(() -> callBack.onAccountStatsLoaded(accountStats));
        });
    }

    @Override
    public void changeCurrencyPosition(Currency a, Currency b) {
        execute(() -> {
            dbLock.writeLock().lock();
            Log.d(TAG, "changeCurrencyPosition: in " + currentThread().getName());
            Integer i = a.getId();
            Integer j = b.getId();
            a.setId(j);
            b.setId(i);
            mCurrencyRateDao.updateCurrencyId(a.getName(), -1);
            mCurrencyRateDao.updateCurrencyId(b.getName(), i);
            mCurrencyRateDao.updateCurrencyId(a.getName(), j);
            dbLock.writeLock().unlock();
        });
    }

    @Override
    public void updateCurrencies(Context context, UpdateCallback callback) {
        mExecutors.networkIO().execute(() -> {
            CurrencyRequest request = new CurrencyRequest(context);
            if (request.checkConnection()) {

                String timestamp = PreferenceUtil.getString(context, LAST_UPDATE_TIMESTAMP, "0");
                DateTime before = new DateTime(Long.valueOf(timestamp) * 1000);
                DateTime after = DateTime.now();

                if (!after.minusDays(1).isAfter(before)) {
                    mExecutors.mainThread().execute(() -> callback.connectFailed("update date too close"));
                    return;
                }

                CurrenciesInfo currenciesInfo = request.getCurrenciesInfo();
                if ("false".equals(currenciesInfo.getSuccess())) {
                    mExecutors.mainThread().execute(() -> callback.connectFailed(currenciesInfo.getError().toString()));
                    return;
                }

                PreferenceUtil.setString(context, LAST_UPDATE_TIMESTAMP, currenciesInfo.getTimestamp());

                List<Currency> currencies = currenciesInfo.getQuotes();
                for (Currency currency : currencies) {
                    Currency rawCurrency = mCurrencyRateDao.getCurrency(currency.getName());
                    if (rawCurrency != null) {
                        rawCurrency.setExchangeRate(currency.getExchangeRate());
                        mCurrencyRateDao.updateCurrency(rawCurrency);
                    } else {
                        currency.setFavourite(false);
                        mCurrencyRateDao.addCurrency(currency);
                    }
                }

                mExecutors.mainThread().execute(callback::updated);
            } else {
                mExecutors.mainThread().execute(() -> callback.connectFailed("connect failed"));
            }
        });
    }

    @Override
    public void getCurrencyInfo(String name, LoadCurrencyInfoCallback callback) {
        execute(() -> {
            CurrencyInfo info = mCurrencyInfoDao.getInfo(name);
            if (info != null) {
                mExecutors.mainThread().execute(() -> callback.onCurrencyInfoLoaded(info));
            } else {
                mExecutors.mainThread().execute(callback::onDataUnavailable);
            }
        });
    }

    @Override
    public void getFavouriteCurrenciesInfo(LoadCurrenciesInfoCallback callback) {
        execute(() -> {
            List<Currency> favouriteCurrencies = mCurrencyRateDao.getFavouriteCurrencies(true);
            List<CurrencyInfo> infos = new ArrayList<>();
            for (Currency currency : favouriteCurrencies) {
                CurrencyInfo info = mCurrencyInfoDao.getInfo(currency.getName());
                infos.add(info);
            }
            if (infos.isEmpty()) {
                mExecutors.mainThread().execute(callback::onDataUnavailable);
            } else {
                mExecutors.mainThread().execute(() -> callback.onCurrenciesInfoLoaded(infos));
            }
        });
    }

    @Override
    public void getFavouriteCurrenciesExchangeRate(String from, LoadExchangeRatesCallback callback) {
        execute(() -> {
            Currency currencyFrom = mCurrencyRateDao.getCurrency(from);
            List<Currency> favouriteCurrencies = mCurrencyRateDao.getFavouriteCurrencies(true);
            if (currencyFrom != null && favouriteCurrencies != null && !favouriteCurrencies.isEmpty()) {
                for (Currency currencyTo : favouriteCurrencies) {
                    Double rate = currencyTo.getExchangeRate() / currencyFrom.getExchangeRate();
                    currencyTo.setExchangeRate(rate);
                    currencyTo.setSource(from);
                }
                mExecutors.mainThread().execute(() -> callback.onExchangeRatesLoaded(favouriteCurrencies));
            } else {
                mExecutors.mainThread().execute(callback::onDataUnavailable);
            }
        });
    }

    @Override
    public void getCurrenciesExchangeRate(String from, LoadExchangeRatesCallback callback) {
        execute(() -> {
            Currency currencyFrom = mCurrencyRateDao.getCurrency(from);
            List<Currency> currencies = mCurrencyRateDao.getCurrencies();
            if (currencyFrom != null && currencies != null && !currencies.isEmpty()) {
                for (Currency currencyTo : currencies) {
                    Double rate = currencyTo.getExchangeRate() / currencyFrom.getExchangeRate();
                    currencyTo.setExchangeRate(rate);
                    currencyTo.setSource(from);
                }
                mExecutors.mainThread().execute(() -> callback.onExchangeRatesLoaded(currencies));
            } else {
                mExecutors.mainThread().execute(callback::onDataUnavailable);
            }
        });
    }

    @Override
    public void getCurrencyExchangeRate(String from, String to, LoadExchangeRateCallback callback) {
        execute(() -> {
            Currency currencyFrom = mCurrencyRateDao.getCurrency(from);
            Currency currencyTo = mCurrencyRateDao.getCurrency(to);
            if (currencyFrom != null && currencyTo != null) {
                Double rate = currencyTo.getExchangeRate() / currencyFrom.getExchangeRate();
                Currency currency = new Currency(from, to, rate);
                mExecutors.mainThread().execute(() -> callback.onExchangeRateLoaded(currency));
            } else {
                mExecutors.mainThread().execute(callback::onDataUnavailable);
            }
        });
    }

    @Override
    public void getCurrency(String name, LoadExchangeRateCallback callback) {
        execute(() -> {
            Currency currency = mCurrencyRateDao.getCurrency(name);
            if (currency == null) {
                mExecutors.mainThread().execute(callback::onDataUnavailable);
            } else {
                mExecutors.mainThread().execute(() -> callback.onExchangeRateLoaded(currency));
            }
        });
    }

    public void initDb() {
        execute(() -> {
            Log.d(TAG, "initDb: in " + currentThread().getName());
            dbLock.writeLock().lock();
            mAccountDao.newAccount(new Account("现金", "现金金额",
                    BigDecimal.ZERO, "cash.png", R.color.amber500));
            mAccountDao.newAccount(new Account("支付宝", "在线支付余额",
                    BigDecimal.ZERO, "alipay.png", R.color.lightblue500));
            mAccountDao.newAccount(new Account("微信", "在线支付余额",
                    BigDecimal.ZERO, "wechat.png", R.color.lightgreen500));
            mTypeDao.newType(new Type("吃喝", Color.rgb(0xe6, 0xc4, 0x53),
                    true, "diet.png"));
            mTypeDao.newType(new Type("娱乐", Color.rgb(0x73, 0xc8, 0xd5),
                    true, "entertainment.png"));
            mTypeDao.newType(new Type("交通", Color.rgb(0xf9, 0xd5, 0x5d),
                    true, "traffic.png"));
            mTypeDao.newType(new Type("日用品", Color.rgb(0xf0, 0x8d, 0x78),
                    true, "daily_necessities.png"));
            mTypeDao.newType(new Type("化妆护肤", Color.rgb(0xfe, 0x4c, 0x5e),
                    true, "make_up.png"));
            mTypeDao.newType(new Type("医疗", Color.rgb(0xc0, 0xf1, 0xf9),
                    true, "medical.png"));
            mTypeDao.newType(new Type("服饰", Color.rgb(0x7e, 0xb7, 0x9f),
                    true, "apparel.png"));
            mTypeDao.newType(new Type("话费", Color.rgb(0x83, 0x6c, 0xab),
                    true, "calls.png"));
            mTypeDao.newType(new Type("红包", Color.rgb(0xf7, 0x2e, 0x42),
                    true, "red_package.png"));
            mTypeDao.newType(new Type("其他", Color.rgb(0xcd, 0x53, 0x3b),
                    true, "other.png"));
            mTypeDao.newType(new Type("工资", Color.rgb(0x97, 0x73, 0x69),
                    false, "wage.png"));
            mTypeDao.newType(new Type("兼职", Color.rgb(0xa7, 0xee, 0xf9),
                    false, "part_time.png"));
            mTypeDao.newType(new Type("奖金", Color.rgb(0xf4, 0xbc, 0xb1),
                    false, "prize.png"));
            mTypeDao.newType(new Type("理财投资", Color.rgb(0xff, 0xec, 0xab),
                    false, "invest.png"));
            mTypeDao.newType(new Type("红包", Color.rgb(0xf7, 0x2e, 0x42),
                    false, "red_package.png"));
            mTypeDao.newType(new Type("其他", Color.rgb(0xcd, 0x53, 0x3b),
                    false, "other.png"));
            dbLock.writeLock().unlock();
        });
    }

    @Override
    public void initCurrenciesAndInfos(Context context) {
        execute(() -> {
            try (InputStream inputStream = context.getResources().getAssets().open("currency/default_rate.json")) {
                Reader reader = new InputStreamReader(inputStream);
                CurrenciesInfo currenciesInfo = JsonConvertor.toCurrenciesInfo(reader);
                PreferenceUtil.setString(context, LAST_UPDATE_TIMESTAMP, currenciesInfo.getTimestamp());
                for (Currency currency : currenciesInfo.getQuotes()) {
                    switch (currency.getName()) {
                        case "CNY":
                        case "USD":
                        case "HKD":
                        case "JPY":
                        case "MOP":
                        case "TWD":
                        case "CAD":
                        case "EUR":
                        case "GBP":
                        case "AUD":
                            currency.setFavourite(Boolean.TRUE);
                            break;
                        default:
                            currency.setFavourite(Boolean.FALSE);
                    }
                    mCurrencyRateDao.addCurrency(currency);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateCurrencies(context, new UpdateCallback() {
                @Override
                public void connectFailed(String msg) {
                    Log.e(TAG, "connectFailed: " + msg);
                }

                @Override
                public void updated() {
                    Log.d(TAG, "updated: ");
                }
            });

            try {
                InputStream nameIs = context.getResources().getAssets().open("currency/name.json");
                InputStream translationCnIs = context.getResources().getAssets().open("currency/translation_cn.json");
                String flagPath = "currency/flag";
                String[] flags = context.getResources().getAssets().list(flagPath);
                Reader nameReader = new InputStreamReader(nameIs);
                Reader translationCnReader = new InputStreamReader(translationCnIs);
                Map<String, String> codeMap = JsonConvertor.toCurrencyCodeMap(nameReader);
                Map<String, String> translationCnCodeMap = JsonConvertor.toCurrencyCodeMap(translationCnReader);
                Map<String, String> flagsMap = new HashMap<>(Objects.requireNonNull(flags).length);

                for (String s : flags) {
                    String key = s.replace(".png", "");
                    flagsMap.put(key, flagPath + "/" + s);
                }

                for (String key : codeMap.keySet()) {
                    CurrencyInfo info = new CurrencyInfo();
                    info.setName(key);
                    info.setFlagLocation(flagsMap.get(key));
                    info.setFullName(codeMap.get(key));
                    info.setFullNameCN(translationCnCodeMap.get(key));
                    mCurrencyInfoDao.addInfo(info);
                }

                nameIs.close();
                translationCnIs.close();
                nameReader.close();
                translationCnReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void execute(LoadData loadData) {
        Runnable runnable = () -> {
            slowDown();
            loadData.load();
        };
        mExecutors.diskIO().execute(runnable);
    }

    private interface LoadData {
        void load();
    }
}
