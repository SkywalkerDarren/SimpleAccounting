package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.dao.AccountDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.StatsDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.TypeDao;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Stats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

public class AppRepositry implements AppDataSource {
    private static final String TAG = "AppRepositry";
    private AccountDao mAccountDao;
    private TypeDao mTypeDao;
    private BillDao mBillDao;
    private StatsDao mStatsDao;

    private static volatile AppRepositry INSTANCE;

    private AppExecutors mExecutors;

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

    @Deprecated
    @Override
    public Account getAccount(UUID uuid) {
        return mAccountDao.getAccount(uuid);
    }

    @Deprecated
    @Override
    public List<Account> getAccounts() {
        return mAccountDao.getAccounts();
    }

    @Override
    public void getAccount(UUID uuid, LoadAccountCallBack callBack) {
        execute(() -> {
            Account account = mAccountDao.getAccount(uuid);
            mExecutors.mainThread().execute(() -> callBack.onAccountLoaded(account));
        });
    }

    @Override
    public void getAccounts(LoadAccountsCallBack callBack) {
        execute(() -> {
            List<Account> accounts = mAccountDao.getAccounts();
            mExecutors.mainThread().execute(() -> callBack.onAccountsLoaded(accounts));
        });
    }

    @Override
    public void updateAccountId(UUID uuid, Integer id) {
        execute(() -> mAccountDao.updateAccountId(uuid, id));
    }

    @Override
    public void delAccount(UUID uuid) {
        execute(() -> mAccountDao.delAccount(uuid));
    }

    @Override
    public void changePosition(Account a, Account b) {
        execute(() -> {
            Integer i = a.getId();
            Integer j = b.getId();
            mAccountDao.updateAccountId(a.getUUID(), -1);
            mAccountDao.updateAccountId(b.getUUID(), i);
            mAccountDao.updateAccountId(a.getUUID(), j);
        });
    }

    @Deprecated
    @Override
    public Bill getBill(UUID id) {
        return mBillDao.getBill(id);
    }

    @Deprecated
    @Override
    public List<Bill> getsBills(int year, int month) {
        DateTime start = new DateTime(year, month, 1, 1, 0, 0);
        DateTime end = start.plusMonths(1);
        return mBillDao.getsBillsByDate(start, end);
    }

    @Override
    public void getBill(UUID id, LoadBillCallBack callBack) {
        execute(() -> {
            Bill bill = mBillDao.getBill(id);
            mExecutors.mainThread().execute(() -> callBack.onBillLoaded(bill));
        });
    }

    @Override
    public void getsBills(int year, int month, LoadBillsCallBack callBack) {
        execute(() -> {
            DateTime start = new DateTime(year, month, 1, 1, 0, 0);
            DateTime end = start.plusMonths(1);
            List<Bill> bills = mBillDao.getsBillsByDate(start, end);
            mExecutors.mainThread().execute(() -> callBack.onBillsLoaded(bills));
        });
    }

    @Override
    public void addBill(Bill bill) {
        execute(() -> mBillDao.addBill(bill));
    }

    @Override
    public void delBill(UUID id) {
        execute(() -> mBillDao.delBill(id));
    }

    @Override
    public void updateBill(Bill bill) {
        execute(() -> mBillDao.updateBill(bill));
    }

    @Override
    public void clearBill() {
        execute(() -> mBillDao.clearBill());
    }

    @Deprecated
    @Override
    public Type getType(UUID uuid) {
        return mTypeDao.getType(uuid);
    }

    @Deprecated
    @Override
    public List<Type> getTypes(boolean isExpense) {
        return mTypeDao.getTypes(isExpense);
    }

    @Override
    public void getType(UUID uuid, LoadTypeCallBack callBack) {
        execute(() -> {
            Type type = mTypeDao.getType(uuid);
            mExecutors.mainThread().execute(() -> callBack.onTypeLoaded(type));
        });
    }

    @Override
    public void getTypes(boolean isExpense, LoadTypesCallBack callBack) {
        execute(() -> {
            List<Type> types = mTypeDao.getTypes(isExpense);
            mExecutors.mainThread().execute(() -> callBack.onTypesLoaded(types));
        });
    }

    @Override
    public void delType(UUID uuid) {
        execute(() -> mTypeDao.delType(uuid));
    }

    @Deprecated
    @Override
    public List<BillStats> getAnnualStats(int year) {
        DateTime start = new DateTime(year, 1, 1, 0, 0, 0);
        DateTime end = start.plusMonths(1);
        List<BillStats> billStatsList = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            BillStats billStats = getBillStats(start, end);
            billStatsList.add(billStats);
            start = start.plusMonths(1);
            end = end.plusMonths(1);
        }
        return billStatsList;
    }

    @Deprecated
    @Override
    public List<BillStats> getMonthStats(int year, int month) {
        List<BillStats> statsList = new ArrayList<>(31);
        DateTime start = new DateTime(year, month, 1, 0, 0);
        DateTime end = start.plusDays(1);
        int days = start.plusMonths(1).minus(1L).getDayOfMonth();
        for (int i = 1; i <= days; i++) {
            BillStats billStats = getBillStats(start, end);
            statsList.add(billStats);
            start = start.plusDays(1);
            end = end.plusDays(1);
        }
        return statsList;
    }

    @Deprecated
    @Override
    public List<TypeStats> getTypesStats(DateTime start, DateTime end, boolean isExpense) {
        return mStatsDao.getTypesStats(start, end, isExpense);
    }

    @Deprecated
    @Override
    public BigDecimal getTypeStats(DateTime start, DateTime end, UUID typeId) {
        return mStatsDao.getTypeStats(start, end, typeId).getBalance();
    }

    @Deprecated
    @Override
    public List<AccountStats> getAccountStats(UUID accountId, int year) {
        DateTime start = new DateTime(year, 1, 1, 0, 0, 0);
        DateTime end = start.plusMonths(1);
        List<AccountStats> accountStatsList = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            AccountStats accountStats = getAccountStats(accountId, start, end);
            accountStatsList.add(accountStats);
            start = start.plusMonths(1);
            end = end.plusMonths(1);
        }
        return accountStatsList;
    }

    @Deprecated
    @Override
    public AccountStats getAccountStats(UUID accountId, DateTime start, DateTime end) {
        List<Stats> stats = mStatsDao.getAccountStats(accountId, start, end);
        AccountStats accountStats = new AccountStats();
        for (Stats s : stats) {
            if (s.getExpense()) {
                accountStats.setExpense(s.getBalance());
            } else {
                accountStats.setIncome(s.getBalance());
            }
        }
        return accountStats;
    }

    @Deprecated
    @Override
    public BillStats getBillStats(DateTime start, DateTime end) {
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

    @Deprecated
    @Override
    public BigDecimal getTypeAverage(DateTime start, DateTime end, UUID typeId) {
        return mStatsDao.getTypeAverageStats(start, end, typeId).getBalance();
    }

    @Override
    public void getBillsAnnualStats(int year, LoadBillsStatsCallBack callBack) {
        execute(() -> {
            DateTime start = new DateTime(year, 1, 1, 0, 0, 0);
            DateTime end = start.plusMonths(1);
            List<BillStats> billStatsList = new ArrayList<>(12);
            for (int i = 1; i <= 12; i++) {
                BillStats billStats = getBillStats(start, end);
                billStatsList.add(billStats);
                start = start.plusMonths(1);
                end = end.plusMonths(1);
            }
            mExecutors.mainThread().execute(() -> callBack.onBillStatsLoaded(billStatsList));
        });
    }

    @Override
    public void getBillsMonthStats(int year, int month, LoadBillsStatsCallBack callBack) {
        execute(() -> {
            List<BillStats> statsList = new ArrayList<>(31);
            DateTime start = new DateTime(year, month, 1, 0, 0);
            DateTime end = start.plusDays(1);
            int days = start.plusMonths(1).minus(1L).getDayOfMonth();
            for (int i = 1; i <= days; i++) {
                BillStats billStats = getBillStats(start, end);
                statsList.add(billStats);
                start = start.plusDays(1);
                end = end.plusDays(1);
            }
            mExecutors.mainThread().execute(() -> callBack.onBillStatsLoaded(statsList));
        });
    }

    @Override
    public void getBillStats(DateTime start, DateTime end, LoadBillStatsCallBack callBack) {
        execute(() -> {
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
            List<TypeStats> typesStats = mStatsDao.getTypesStats(start, end, isExpense);
            mExecutors.mainThread().execute(() -> callBack.onTypesStatsLoaded(typesStats));
        });
    }

    @Override
    public void getTypeStats(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack) {
        execute(() -> {
            TypeStats typeStats = mStatsDao.getTypeStats(start, end, typeId);
            mExecutors.mainThread().execute(() -> callBack.onTypeStatsLoaded(typeStats));
        });
    }

    @Override
    public void getTypeAverage(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack) {
        execute(() -> {
            TypeStats typeStats = mStatsDao.getTypeAverageStats(start, end, typeId);
            mExecutors.mainThread().execute(() -> callBack.onTypeStatsLoaded(typeStats));
        });
    }

    @Override
    public void getAccountsStats(UUID accountId, int year, LoadAccountsStatsCallBack callBack) {
        execute(() -> {
            DateTime start = new DateTime(year, 1, 1, 0, 0, 0);
            DateTime end = start.plusMonths(1);
            List<AccountStats> accountStatsList = new ArrayList<>(12);
            for (int i = 1; i <= 12; i++) {
                AccountStats accountStats = getAccountStats(accountId, start, end);
                accountStatsList.add(accountStats);
                start = start.plusMonths(1);
                end = end.plusMonths(1);
            }
            mExecutors.mainThread().execute(() -> callBack.onAccountsStatsLoaded(accountStatsList));
        });
    }

    @Override
    public void getAccountStats(UUID accountId, DateTime start, DateTime end, LoadAccountStatsCallBack callBack) {
        execute(() -> {
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

    private void execute(LoadData loadData) {
        Runnable runnable = loadData::load;
        mExecutors.diskIO().execute(runnable);
    }

    public void initDb() {
        execute(() -> {
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

    private interface LoadData {
        void load();
    }
}
