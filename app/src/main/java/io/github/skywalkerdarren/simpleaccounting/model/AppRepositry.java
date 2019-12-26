package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.DateHeaderDivider;
import io.github.skywalkerdarren.simpleaccounting.model.dao.AccountDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.StatsDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.TypeDao;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Stats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class AppRepositry implements AppDataSource {
    private static final String TAG = "AppRepositry";
    private AccountDao mAccountDao;
    private TypeDao mTypeDao;
    private BillDao mBillDao;
    private StatsDao mStatsDao;

    private static final boolean DEBUG = false;

    private static volatile AppRepositry INSTANCE;

    private AppExecutors mExecutors;

    private void slowDown() {
        if (DEBUG) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private AppRepositry(@NonNull AppExecutors executors, @NonNull Context context) {
        mExecutors = executors;
        AppDatabase database = AppDatabase.getInstance(context);
        mAccountDao = database.accountDao();
        mTypeDao = database.typeDao();
        mBillDao = database.billDao();
        mStatsDao = database.statsDao();
    }

    private AppRepositry(@NonNull AppExecutors executors, AppDatabase database) {
        mAccountDao = database.accountDao();
        mTypeDao = database.typeDao();
        mBillDao = database.billDao();
        mStatsDao = database.statsDao();
        mExecutors = executors;
    }

    public static AppRepositry getInstance(@NonNull AppExecutors executors, @NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppRepositry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepositry(executors, context);
                }
            }
        }
        return INSTANCE;
    }

    public static AppRepositry getInstance(@NonNull AppExecutors executors, AppDatabase database) {
        if (INSTANCE == null) {
            synchronized (AppRepositry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepositry(executors, database);
                }
            }
        }
        return INSTANCE;
    }

    private static final String ACCOUNTS = "ACCOUNTS";
    private static final String INCOME_TYPES = "INCOME_TYPES";
    private static final String EXPENSE_TYPES = "EXPENSE_TYPES";
    private static Map<UUID, Account> sAccountCache = new ConcurrentHashMap<>();
    private static Map<String, List<Account>> sAccountsCache = new ConcurrentHashMap<>();

    void getAccountsOnBackground(LoadAccountsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccountsOnBackground: in " + currentThread().getName());
            List<Account> accounts = mAccountDao.getAccounts();
            callBack.onAccountsLoaded(accounts);
        });
    }

    private static Map<UUID, Bill> sBillCache = new ConcurrentHashMap<>();
    private static Map<UUID, Type> sTypeCache = new ConcurrentHashMap<>();

    @Override
    public void getBillInfoList(int year, int month, LoadBillsInfoCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getBillInfoList: in " + currentThread().getName());
            DateTime start = new DateTime(year, month, 1, 1, 0, 0);
            DateTime end = start.plusMonths(1);
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
            mExecutors.mainThread().execute(() -> callBack.onBillsInfoLoaded(billInfoList));
        });

    }

    private static Map<String, List<Type>> sTypesCache = new ConcurrentHashMap<>();

    @Override
    public void getAccount(UUID uuid, LoadAccountCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccount: in " + currentThread().getName());
            Account account = sAccountCache.get(uuid);
            if (account == null) {
                account = mAccountDao.getAccount(uuid);
                sAccountCache.put(uuid, account);
            }
            mExecutors.mainThread().execute(() -> callBack.onAccountLoaded(sAccountCache.get(uuid)));
        });
    }

    @Override
    public void getsBills(int year, int month, LoadBillsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getsBills: in " + currentThread().getName());
            DateTime start = new DateTime(year, month, 1, 1, 0, 0);
            DateTime end = start.plusMonths(1);
            List<Bill> bills = mBillDao.getsBillsByDate(start, end);
            mExecutors.mainThread().execute(() -> callBack.onBillsLoaded(bills));
        });
    }

    @Override
    public void getAccounts(LoadAccountsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccounts: in " + currentThread().getName());
            List<Account> accounts = sAccountsCache.get(ACCOUNTS);
            if (accounts == null) {
                accounts = mAccountDao.getAccounts();
                sAccountsCache.put(ACCOUNTS, accounts);
            }
            mExecutors.mainThread().execute(() -> callBack.onAccountsLoaded(sAccountsCache.get(ACCOUNTS)));
        });
    }

    @Override
    public void delAccount(UUID uuid) {
        execute(() -> {
            Log.d(TAG, "delAccount: in " + currentThread().getName());
            mAccountDao.delAccount(uuid);
            sAccountsCache.clear();
            sAccountCache.remove(uuid);
        });
    }

    @Override
    public void changePosition(Account a, Account b) {
        execute(() -> {
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

        });
    }

    @Override
    public void getBill(UUID id, LoadBillCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getBill: in " + currentThread().getName());
            Bill bill = sBillCache.get(id);
            if (bill == null) {
                bill = mBillDao.getBill(id);
                try {
                    sBillCache.put(id, bill);
                } catch (NullPointerException ignore) {

                }
            }
            mExecutors.mainThread().execute(() -> callBack.onBillLoaded(sBillCache.get(id)));
        });
    }

    @Override
    public void addBill(Bill bill) {
        execute(() -> {
            Log.d(TAG, "addBill: in " + currentThread().getName());
            mBillDao.addBill(bill);
            sBillCache.put(bill.getUUID(), bill);
        });
    }

    @Override
    public void delBill(UUID id) {
        execute(() -> {
            Log.d(TAG, "delBill: in " + currentThread().getName());
            mBillDao.delBill(id);
            sBillCache.remove(id);
        });
    }

    @Override
    public void updateBill(Bill bill) {
        execute(() -> {
            Log.d(TAG, "updateBill: in " + currentThread().getName());
            mBillDao.updateBill(bill);
            sBillCache.put(bill.getUUID(), bill);
        });
    }

    @Override
    public void clearBill() {
        execute(() -> {
            Log.d(TAG, "clearBill: in " + currentThread().getName());
            mBillDao.clearBill();
            sBillCache.clear();
        });
    }

    @Override
    public void getType(UUID uuid, LoadTypeCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getType: in " + currentThread().getName());
            Type type = sTypeCache.get(uuid);
            if (type == null) {
                type = mTypeDao.getType(uuid);
                sTypeCache.put(uuid, type);
            }
            mExecutors.mainThread().execute(() -> callBack.onTypeLoaded(sTypeCache.get(uuid)));
        });
    }

    @Override
    public void getTypes(boolean isExpense, LoadTypesCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypes: in " + currentThread().getName());
            final String flag = isExpense ? EXPENSE_TYPES : INCOME_TYPES;
            List<Type> types = sTypesCache.get(flag);
            if (types == null) {
                types = mTypeDao.getTypes(isExpense);
                sTypesCache.put(flag, types);
            }
            mExecutors.mainThread().execute(() -> callBack.onTypesLoaded(sTypesCache.get(flag)));
        });
    }

    void getTypesOnBackground(boolean isExpense, LoadTypesCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypesOnBackground: in " + currentThread().getName());
            List<Type> types = mTypeDao.getTypes(isExpense);
            callBack.onTypesLoaded(types);
        });
    }

    @Override
    public void delType(UUID uuid) {
        execute(() -> {
            Log.d(TAG, "delType: in " + currentThread().getName());
            mTypeDao.delType(uuid);
            sTypeCache.remove(uuid);
            sTypesCache.clear();
        });
    }

    private BillStats getBillStats(DateTime start, DateTime end) {
        List<Stats> stats = mStatsDao.getBillsStats(start, end);
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
            mExecutors.mainThread().execute(() -> callBack.onBillStatsLoaded(billStatsList));
        });
    }

    @Override
    public void getBillStats(DateTime start, DateTime end, LoadBillStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getBillStats: in " + currentThread().getName());
            List<Stats> stats = mStatsDao.getBillsStats(start, end);
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
            List<TypeStats> typesStats = mStatsDao.getTypesStats(start, end, isExpense);
            mExecutors.mainThread().execute(() -> callBack.onTypesStatsLoaded(typesStats));
        });
    }

    @Override
    public void getTypeStats(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypeStats: in " + currentThread().getName());
            TypeStats typeStats = mStatsDao.getTypeStats(start, end, typeId);
            mExecutors.mainThread().execute(() -> callBack.onTypeStatsLoaded(typeStats));
        });
    }

    @Override
    public void getTypeAverage(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getTypeAverage: in " + currentThread().getName());
            TypeStats typeStats = mStatsDao.getTypeAverageStats(start, end, typeId);
            mExecutors.mainThread().execute(() -> callBack.onTypeStatsLoaded(typeStats));
        });
    }

    @Override
    public void getAccountStats(UUID accountId, DateTime start, DateTime end, LoadAccountStatsCallBack callBack) {
        execute(() -> {
            Log.d(TAG, "getAccountStats: in " + currentThread().getName());
            List<Stats> stats = mStatsDao.getAccountStats(accountId, start, end);
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

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }

    public void initDb() {
        execute(() -> {
            Log.d(TAG, "initDb: in " + currentThread().getName());
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
