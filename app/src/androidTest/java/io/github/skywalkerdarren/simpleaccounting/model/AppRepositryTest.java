package io.github.skywalkerdarren.simpleaccounting.model;

import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.util.SingleExecutors;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppRepositryTest {
    private static final String TAG = "AppRepositryTest";
    private AppRepositry mRepositry;
    private static final Account ACCOUNT = new Account("name", "balanceHint", BigDecimal.ZERO, "image", R.color.black);
    private static final Type TYPE = new Type("TYPE", R.color.darkorchid, true, "assetsName");
    private final DateTime now = DateTime.now();
    private Bill mBill2;
    private AppDatabase mDatabase = Room.inMemoryDatabaseBuilder(getApplicationContext(),
            AppDatabase.class)
            .build();
    private Bill mBill1;

    private void assertAccount1(Account account) {
        assertThat(account.getName(), is("name1"));
        assertThat(account.getBalanceHint(), is("balanceHint1"));
        assertThat(account.getBalance(), is(BigDecimal.ZERO));
        assertThat(account.getBitmap(), is("image1"));
        assertThat(account.getColorId(), is(R.color.black));
    }

    private void assertBill(Bill expect, Bill actual) {
        assertThat(expect.getAccountId(), is(actual.getAccountId()));
        assertThat(expect.getBalance(), is(actual.getBalance()));
        assertThat(expect.getName(), is(actual.getName()));
        assertThat(expect.getDate(), is(actual.getDate()));
    }

    private void assertType(Type expect) {
        assertThat(expect.getUUID(), is(mBill1.getTypeId()));
    }

    @Before
    public void setUp() {
        Account account1 = new Account("name1", "balanceHint1", BigDecimal.ZERO, "image1", R.color.black);
        Account account2 = new Account("name2", "balanceHint2", BigDecimal.ONE, "image2", R.color.white);
        Type type1 = new Type("name1", R.color.darkorchid, true, "assetsName");
        Type type2 = new Type("name2", R.color.darkorchid, false, "assetsName");
        mBill1 = new Bill(type1.getUUID(), account1.getUUID(), now, "name1", new BigDecimal(100), "remark");
        mBill2 = new Bill(type2.getUUID(), account2.getUUID(), now.minusDays(1), "name2", new BigDecimal(200), "remark");

        AppRepositry.clearInstance();
        mRepositry = AppRepositry.getInstance(new SingleExecutors(), mDatabase);
        mDatabase.accountDao().newAccount(account1);
        mDatabase.accountDao().newAccount(account2);
        mDatabase.accountDao().newAccount(ACCOUNT);
        mDatabase.typeDao().newType(type1);
        mDatabase.typeDao().newType(type2);
        mDatabase.typeDao().newType(TYPE);
        mDatabase.billDao().addBill(mBill1);
        mDatabase.billDao().addBill(mBill2);
    }

    @After
    public void tearDown() {
        mDatabase.close();
        AppRepositry.clearInstance();
    }

    @Test
    public void getBillInfoList() {
        mRepositry.getBillInfoList(now.getYear(), now.getMonthOfYear(), billsInfo -> {
            assertEquals(4, billsInfo.size());
            for (BillInfo billInfo : billsInfo) {
                Log.d(TAG, "getBillInfoList: " + billInfo);
            }
        });
    }

    @Test
    public void getAccount() {
        mRepositry.getAccount(mBill1.getAccountId(), this::assertAccount1);
    }

    @Test
    public void getsBills() {
        mRepositry.getsBills(now.getYear(), now.getMonthOfYear(), bills ->
                assertEquals(2, bills.size()));
    }

    @Test
    public void getAccounts() {
        mRepositry.getAccounts(accounts -> assertEquals(3, accounts.size()));
    }

    @Test
    public void delAccount() {
        mRepositry.delAccount(mBill1.getAccountId());
        mRepositry.getAccounts(accounts -> assertEquals(2, accounts.size()));
    }

    @Test
    public void changePosition() {
        int[] id = new int[2];
        mRepositry.getAccount(mBill1.getAccountId(), a ->
                mRepositry.getAccount(mBill2.getAccountId(), b -> {
                    id[0] = a.getId();
                    id[1] = b.getId();
                    mRepositry.changePosition(a, b);
                }));
        mRepositry.getAccount(mBill1.getAccountId(), a ->
                mRepositry.getAccount(mBill2.getAccountId(), b -> {
                    assertEquals(id[1], a.getId().intValue());
                    assertEquals(id[0], b.getId().intValue());
                }));
    }

    @Test
    public void getBill() {
        mRepositry.getBill(mBill1.getUUID(), bill -> assertBill(mBill1, bill));
        mRepositry.getBill(mBill2.getUUID(), bill -> assertBill(mBill2, bill));
    }

    @Test
    public void addBill() {
        Bill bill = new Bill(TYPE.getUUID(), ACCOUNT.getUUID(), now, "name", BigDecimal.ZERO, "remark");
        mRepositry.addBill(bill);
        mRepositry.getBill(bill.getUUID(), b -> assertBill(bill, b));
    }

    @Test
    public void delBill() {
        mRepositry.delBill(mBill2.getUUID());
        mRepositry.getsBills(now.getYear(), now.getMonthOfYear(), bills ->
                assertEquals(1, bills.size()));
    }

    @Test
    public void updateBill() {
        mBill1.setTypeId(TYPE.getUUID());
        mBill1.setAccountId(ACCOUNT.getUUID());
        mBill1.setBalance(BigDecimal.ONE);
        mBill1.setName("name");
        mBill1.setDate(now.plusDays(1));
        mBill1.setRemark("update");
        mRepositry.updateBill(mBill1);
        mRepositry.getBill(mBill1.getUUID(), bill -> assertBill(mBill1, bill));
    }

    @Test
    public void clearBill() {
        mRepositry.clearBill();
        mRepositry.getsBills(now.getYear(), now.getMonthOfYear(), bills ->
                assertEquals(0, bills.size()));
    }

    @Test
    public void getType() {
        mRepositry.getType(mBill1.getTypeId(), this::assertType);
    }

    @Test
    public void getTypes() {
        mRepositry.getTypes(true, types -> assertEquals(2, types.size()));
    }

    @Test
    public void delType() {
        mRepositry.delType(mBill1.getTypeId());
        mRepositry.getTypes(true, types ->
                assertEquals(1, types.size()));
    }

    @Test
    public void getBillsAnnualStats() {
        mRepositry.getBillsAnnualStats(now.getYear(), billsStats -> {
            assertEquals(12, billsStats.size());
            for (BillStats billStats : billsStats) {
                Log.d(TAG, "getBillsAnnualStats: " + billStats);
            }
        });
    }

    @Test
    public void getBillStats() {
        mRepositry.getBillStats(now.minusDays(2), now.plusDays(2), billStats -> {
            assertEquals(100, billStats.getSum().intValue());
            assertEquals(200, billStats.getIncome().intValue());
            assertEquals(100, billStats.getExpense().intValue());
        });
    }

    @Test
    public void getTypesStats() {
        mRepositry.getTypesStats(now.minusDays(2), now.plusDays(2), true, typesStats ->
                assertEquals(1, typesStats.size()));
        mRepositry.getTypesStats(now.minusDays(2), now.plusDays(2), false, typesStats ->
                assertEquals(1, typesStats.size()));
    }

    @Test
    public void getTypeStats() {
        mRepositry.getTypeStats(now.minusDays(2), now.plusDays(2), mBill1.getTypeId(), typeStats ->
                assertEquals(100, typeStats.getBalance().intValue()));
    }

    @Test
    public void getTypeAverage() {
        mRepositry.getTypeStats(now.minusDays(2), now.plusDays(2), mBill1.getTypeId(), typeStats ->
                assertEquals(100, typeStats.getBalance().intValue()));
    }

    @Test
    public void getAccountStats() {
        mRepositry.getAccountStats(mBill1.getAccountId(), now.minusDays(2), now.plusDays(2), accountStats ->
                assertEquals(-100, accountStats.getSum().intValue()));
    }
}