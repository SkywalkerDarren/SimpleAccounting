package io.github.skywalkerdarren.simpleaccounting.model;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppRepositryTest {
    private static final String TAG = "AppRepositryTest";
    private AppRepositry mRepositry;
    private Bill mBill;
    private Bill mBill2;
    private Account a;
    private Bill b;
    private Type t;

    private AppDatabase mDatabase = Room.inMemoryDatabaseBuilder(getApplicationContext(),
            AppDatabase.class)
            .build();

    private void assertAccount(Account account) {
        assertThat(account.getBalance(), is(BigDecimal.ZERO));
        assertThat(account.getName(), is("name"));
    }

    private void assertBill(Bill expect, Bill actual) {
        assertThat(expect.getAccountId(), is(actual.getAccountId()));
        assertThat(expect.getBalance(), is(actual.getBalance()));
        assertThat(expect.getName(), is(actual.getName()));
    }

    private void assertType(Type expect) {
        assertThat(expect.getUUID(), is(mBill.getTypeId()));
    }

    @Before
    public void setUp() throws Exception {
        Account account = new Account("name", "balanceHint", BigDecimal.ZERO, "image", R.color.black);
        Account account2 = new Account("name2", "balanceHint", BigDecimal.ZERO, "image", R.color.black);
        Type type = new Type("name", R.color.darkorchid, true, "assetsName");
        Type type2 = new Type("name2", R.color.darkorchid, true, "assetsName");
        mBill = new Bill(type.getUUID(), account.getUUID(), new DateTime(), "name", new BigDecimal(100), "remark");
        mBill2 = new Bill(type2.getUUID(), account2.getUUID(), new DateTime(), "name2", new BigDecimal(200), "remark");

        AppRepositry.clearInstance();
        mRepositry = AppRepositry.getInstance(new AppExecutors(), mDatabase);
        mDatabase.accountDao().newAccount(account);
        mDatabase.accountDao().newAccount(account2);
        mDatabase.typeDao().newType(type);
        mDatabase.typeDao().newType(type2);
        mDatabase.billDao().addBill(mBill);
        mDatabase.billDao().addBill(mBill2);
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
        AppRepositry.clearInstance();
    }

    @Test
    public void getAccount() {
        assertAccount(mRepositry.getAccount(mBill.getAccountId()));
    }

    @Test
    public void getAccounts() {
        List<Account> accounts = mRepositry.getAccounts();
        assertAccount(accounts.get(0));
    }

    @Test
    public void updateAccount() throws InterruptedException {
        Account account = mRepositry.getAccount(mBill.getAccountId());
        account.setId(-1);
        mRepositry.updateAccountId(account.getUUID(), -1);
        sleep(10);
        account = mRepositry.getAccount(mBill.getAccountId());
        assertEquals(-1, (int) account.getId());
    }


    @Test
    public void delAccount() {
        mRepositry.delAccount(mBill.getAccountId());

        mRepositry.getAccount(mBill.getAccountId(), account -> a = account);
        assertNull(a);
    }

    @Test
    public void changePosition() {
        Account a = mRepositry.getAccount(mBill.getAccountId());
        Account b = mRepositry.getAccount(mBill2.getAccountId());
        Integer i = a.getId();
        Integer j = b.getId();
        mRepositry.changePosition(a, b);
        a = mRepositry.getAccount(mBill.getAccountId());
        b = mRepositry.getAccount(mBill2.getAccountId());
        assertEquals(a.getId(), j);
        assertEquals(b.getId(), i);
    }

    @Test
    public void getBill() {
        Bill bill = mRepositry.getBill(mBill.getUUID());
        assertBill(mBill, bill);
    }

    @Test
    public void getsBills() {
        DateTime dateTime = new DateTime();
        List<Bill> bills = mRepositry.getsBills(dateTime.getYear(), dateTime.getMonthOfYear());
        assertBill(mBill, bills.get(0));
        assertBill(mBill2, bills.get(1));
    }

    @Test
    public void addBill() {
        mBill.setUUID(UUID.randomUUID());
        mBill.setRemark(null);
        mBill.setId(null);
        mBill.setName(null);
        //mBill.setDate(null);
        mBill.setAccountId(null);
        mBill.setTypeId(null);
        mRepositry.addBill(mBill);
        DateTime dateTime = new DateTime();
        List<Bill> bills = mRepositry.getsBills(dateTime.getYear(), dateTime.getMonthOfYear());
        assertEquals(3, bills.size());
    }

    @Test
    public void delBill() {
        mRepositry.delBill(mBill.getUUID());
        DateTime dateTime = new DateTime();
        List<Bill> bills = mRepositry.getsBills(dateTime.getYear(), dateTime.getMonthOfYear());
        assertEquals(1, bills.size());
    }

    @Test
    public void updateBill() {
        Bill bill = mRepositry.getBill(mBill.getUUID());
        bill.setRemark(null);
        bill.setBalance(BigDecimal.ONE);
        mRepositry.updateBill(bill);
        bill = mRepositry.getBill(mBill.getUUID());
        assertThat(bill.getBalance(), is(BigDecimal.ONE));
        assertNull(bill.getRemark());
    }

    @Test
    public void clearBill() {
        mRepositry.clearBill();
        DateTime dateTime = new DateTime();
        List<Bill> bills = mRepositry.getsBills(dateTime.getYear(), dateTime.getMonthOfYear());
        assertEquals(bills.size(), 0);
    }

    @Test
    public void getType() {
        Type type = mRepositry.getType(mBill.getTypeId());
        assertType(type);
    }

    @Test
    public void getTypes() {
        List<Type> types = mRepositry.getTypes(true);
        assertEquals(types.size(), 2);
        List<Type> types2 = mRepositry.getTypes(false);
        assertEquals(types2.size(), 0);
    }

    @Test
    public void delType() {
        mRepositry.delType(mBill.getTypeId());
        Type type = mRepositry.getType(mBill.getTypeId());
        Bill bill = mRepositry.getBill(mBill.getUUID());
        assertNull(type);
        assertNull(bill);
    }

    @Test
    public void getAnnualStats() {
        DateTime dateTime = DateTime.now();
        List<BillStats> annualStats = mRepositry.getAnnualStats(dateTime.getYear());
        assertEquals(12, annualStats.size());
        for (int i = 0; i < 12; i++) {
            if (i == dateTime.getMonthOfYear() - 1)
                assertEquals(300, annualStats.get(i).getExpense().intValue());
            else
                assertEquals(0, annualStats.get(i).getExpense().intValue());
        }
    }

    @Test
    public void getMonthStats() {
        DateTime dateTime = DateTime.now();
        List<BillStats> billStatsList = mRepositry.getMonthStats(dateTime.getYear(), dateTime.getMonthOfYear());
        assertEquals(31, billStatsList.size());
        BillStats billStats = billStatsList.get(dateTime.getDayOfMonth() - 1);
        assertEquals(300, billStats.getExpense().intValue());
    }

    @Test
    public void getTypesStats() {
        DateTime dateTime = DateTime.now();
        DateTime start = dateTime.minusDays(1);
        DateTime end = dateTime.plusDays(1);
        List<TypeStats> typesStats = mRepositry.getTypesStats(start, end, true);
        assertEquals(2, typesStats.size());
    }

    @Test
    public void getTypeStats() {
        DateTime dateTime = DateTime.now();
        DateTime start = dateTime.minusDays(1);
        DateTime end = dateTime.plusDays(1);
        BigDecimal typeStats = mRepositry.getTypeStats(start, end, mBill.getTypeId());
        assertEquals(100, typeStats.intValue());
    }

    @Test
    public void getAccountStats() {
        DateTime dateTime = DateTime.now();
        DateTime start = dateTime.minusDays(1);
        DateTime end = dateTime.plusDays(1);
        AccountStats accountStats = mRepositry.getAccountStats(mBill.getAccountId(), start, end);
        assertEquals(100,accountStats.getExpense().intValue());
    }

    @Test
    public void getAccountStats1() {
        DateTime dateTime = DateTime.now();
        List<AccountStats> accountStats = mRepositry.getAccountStats(mBill.getAccountId(), dateTime.getYear());
        assertEquals(12, accountStats.size());
        assertEquals(100, accountStats.get(dateTime.getMonthOfYear() - 1).getExpense().intValue());
    }

    @Test
    public void getBillStats() {
        DateTime dateTime = DateTime.now();
        DateTime start = dateTime.minusDays(1);
        DateTime end = dateTime.plusDays(1);
        BillStats billStats = mRepositry.getBillStats(start, end);
        assertEquals(300, billStats.getExpense().intValue());

    }

    @Test
    public void getTypeAverage() {
        DateTime dateTime = DateTime.now();
        DateTime start = dateTime.minusDays(1);
        DateTime end = dateTime.plusDays(1);
        AtomicReference<TypeStats> ts = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        mRepositry.getTypeAverage(start, end, mBill.getTypeId(), typeStats -> {
            ts.set(typeStats);
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(100, ts.get().getBalance().intValue());
    }
}