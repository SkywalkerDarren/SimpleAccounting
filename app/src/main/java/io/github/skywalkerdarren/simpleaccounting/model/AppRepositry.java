package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

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
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Stats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;

public class AppRepositry implements AppDataSource {
    private AccountDao mAccountDao;
    private TypeDao mTypeDao;
    private BillDao mBillDao;
    private StatsDao mStatsDao;

    private static volatile AppRepositry INSTANCE;

    private AppRepositry(@NonNull Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        mAccountDao = database.accountDao();
        mTypeDao = database.typeDao();
        mBillDao = database.billDao();
        mStatsDao = database.statsDao();
    }

    public static AppRepositry getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppRepositry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepositry(context);
                }
            }
        }
        return INSTANCE;
    }

    private AppRepositry(AppDatabase database) {
        mAccountDao = database.accountDao();
        mTypeDao = database.typeDao();
        mBillDao = database.billDao();
        mStatsDao = database.statsDao();
    }

    public static AppRepositry getInstance(AppDatabase database) {
        if (INSTANCE == null) {
            synchronized (AppRepositry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepositry(database);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Account getAccount(UUID uuid) {
        return mAccountDao.getAccount(uuid);
    }

    @Override
    public List<Account> getAccounts() {
        return mAccountDao.getAccounts();
    }

    @Override
    public void updateAccountId(UUID uuid, Integer id) {
        mAccountDao.updateAccountId(uuid, id);
    }

    @Override
    public void delAccount(UUID uuid) {
        mAccountDao.delAccount(uuid);
    }

    @Override
    public void changePosition(Account a, Account b) {
        Integer i = a.getId();
        Integer j = b.getId();
        updateAccountId(a.getUUID(), -1);
        updateAccountId(b.getUUID(), i);
        updateAccountId(a.getUUID(), j);
    }

    @Override
    public Bill getBill(UUID id) {
        return mBillDao.getBill(id);
    }

    @Override
    public List<Bill> getsBills(int year, int month) {
        DateTime start = new DateTime(year, month, 1,1,0,0);
        DateTime end = start.plusMonths(1);
        return mBillDao.getsBillsByDate(start, end);
    }

    @Override
    public void addBill(Bill bill) {
        mBillDao.addBill(bill);
    }

    @Override
    public void delBill(UUID id) {
        mBillDao.delBill(id);
    }

    @Override
    public void updateBill(Bill bill) {
        mBillDao.updateBill(bill);
    }

    @Override
    public void clearBill() {
        mBillDao.clearBill();
    }

    @Override
    public Type getType(UUID uuid) {
        return mTypeDao.getType(uuid);
    }

    @Override
    public List<Type> getTypes(boolean isExpense) {
        return mTypeDao.getTypes(isExpense);
    }

    @Override
    public void delType(UUID uuid) {
        mTypeDao.delType(uuid);
    }

    @Override
    public List<BillStats> getAnnualStats(int year) {
        DateTime start = new DateTime(year,1,1,0,0,0);
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

    @Override
    public List<TypeStats> getTypesStats(DateTime start, DateTime end, boolean isExpense) {
        return mStatsDao.getTypesStats(start, end, isExpense);
    }

    @Override
    public BigDecimal getTypeStats(DateTime start, DateTime end, UUID typeId) {
        return mStatsDao.getTypeStats(start, end, typeId).getBalance();
    }

    @Override
    public List<AccountStats> getAccountStats(UUID accountId, int year) {
        DateTime start = new DateTime(year,1,1,0,0,0);
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

    @Override
    public AccountStats getAccountStats(UUID accountId, DateTime start, DateTime end) {
        List<Stats> stats = mStatsDao.getAccountStats(accountId, start, end);
        AccountStats accountStats = new AccountStats();
        for (Stats s: stats) {
            if (s.getExpense()) {
                accountStats.setExpense(s.getBalance());
            } else {
                accountStats.setIncome(s.getBalance());
            }
        }
        return accountStats;
    }

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

    @Override
    public BigDecimal getTypeAverage(DateTime start, DateTime end, UUID typeId) {
        return mStatsDao.getTypeAverageStats(start, end, typeId).getBalance();
    }

    public static void clearInstance() {
        INSTANCE = null;
    }

    public void initDb() {
        mAccountDao.newAccount(new Account("现金","现金金额",
                BigDecimal.ZERO,"cash.png", R.color.amber500));
        mAccountDao.newAccount(new Account("支付宝","在线支付余额",
                BigDecimal.ZERO,"alipay.png", R.color.lightblue500));
        mAccountDao.newAccount(new Account("微信","在线支付余额",
                BigDecimal.ZERO,"wechat.png", R.color.lightgreen500));
        mTypeDao.newType(new Type("吃喝", Color.rgb(0xe6, 0xc4, 0x53),
                true,"diet.png"));
        mTypeDao.newType(new Type("娱乐", Color.rgb(0x73, 0xc8, 0xd5),
                true,"entertainment.png"));
        mTypeDao.newType(new Type("交通", Color.rgb(0xf9, 0xd5, 0x5d),
                true,"traffic.png"));
        mTypeDao.newType(new Type("日用品", Color.rgb(0xf0, 0x8d, 0x78),
                true,"daily_necessities.png"));
        mTypeDao.newType(new Type("化妆护肤", Color.rgb(0xfe, 0x4c, 0x5e),
                true,"make_up.png"));
        mTypeDao.newType(new Type("医疗", Color.rgb(0xc0, 0xf1, 0xf9),
                true,"medical.png"));
        mTypeDao.newType(new Type("服饰", Color.rgb(0x7e, 0xb7, 0x9f),
                true,"apparel.png"));
        mTypeDao.newType(new Type("话费", Color.rgb(0x83, 0x6c, 0xab),
                true,"calls.png"));
        mTypeDao.newType(new Type("红包", Color.rgb(0xf7, 0x2e, 0x42),
                true,"red_package.png"));
        mTypeDao.newType(new Type("其他", Color.rgb(0xcd, 0x53, 0x3b),
                true,"other.png"));
        mTypeDao.newType(new Type("工资", Color.rgb(0x97, 0x73, 0x69),
                false,"wage.png"));
        mTypeDao.newType(new Type("兼职", Color.rgb(0xa7, 0xee, 0xf9),
                false,"part_time.png"));
        mTypeDao.newType(new Type("奖金", Color.rgb(0xf4, 0xbc, 0xb1),
                false,"prize.png"));
        mTypeDao.newType(new Type("理财投资", Color.rgb(0xff, 0xec, 0xab),
                false,"invest.png"));
        mTypeDao.newType(new Type("红包", Color.rgb(0xf7, 0x2e, 0x42),
                false,"red_package.png"));
        mTypeDao.newType(new Type("其他", Color.rgb(0xcd, 0x53, 0x3b),
                false,"other.png"));
    }
}
