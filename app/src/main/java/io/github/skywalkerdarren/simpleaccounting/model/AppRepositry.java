package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public Account getAccount(UUID uuid) {
        return mAccountDao.getAccount(uuid);
    }

    @Override
    public List<Account> getAccounts() {
        return mAccountDao.getAccounts();
    }

    @Override
    public void updateAccount(Account account) {
        mAccountDao.updateAccount(account);
    }

    @Override
    public void delAccount(UUID uuid) {
        mAccountDao.delAccount(new Account(uuid));
    }

    @Override
    public void changePosition(Account a, Account b) {
        Integer i = a.getId();
        Integer j = b.getId();
        a.setId(-1);
        updateAccount(a);
        b.setId(i);
        updateAccount(b);
        a.setId(j);
        updateAccount(a);
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
        mBillDao.delBill(new Bill(id));
    }

    @Override
    public void updateBill(Bill bill) {
        mBillDao.updateBill(bill);
    }

    @Override
    public void clearBill() {
        mBillDao.delBill(null);
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
        mTypeDao.delType(new Type(uuid));
    }

    @Override
    public List<BillStats> getAnnualStats(int year) {
        DateTime start = new DateTime(year,1,1,0,0,0);
        DateTime end = start.plusMonths(1);
        List<BillStats> billStatsList = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            BillStats billStats = getBillStats(start, end);
            billStatsList.add(billStats);
            start.plusMonths(1);
            end.plusMonths(1);
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
            start.plusDays(1);
            end.plusDays(1);
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
            start.plusMonths(1);
            end.plusMonths(1);
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
}
