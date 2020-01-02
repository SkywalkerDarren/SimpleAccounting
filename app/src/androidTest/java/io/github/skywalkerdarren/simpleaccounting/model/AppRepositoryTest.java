package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.util.SingleExecutors;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppRepositoryTest {
    private static final String TAG = "AppRepositoryTest";
    private static final Account ACCOUNT = new Account("name", "balanceHint", BigDecimal.ZERO, R.color.black, "image");
    private static final Type TYPE = new Type("TYPE", R.color.darkorchid, true, "assetsName");
    private final DateTime now = DateTime.now();
    private AppRepository mRepository;
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
        assertThat(expect.getUuid(), is(mBill1.getTypeId()));
    }

    @Before
    public void setUp() {
        Account account1 = new Account("name1", "balanceHint1", BigDecimal.ZERO, R.color.black, "image1");
        Account account2 = new Account("name2", "balanceHint2", BigDecimal.ONE, R.color.white, "image2");
        Type type1 = new Type("name1", R.color.darkorchid, true, "assetsName");
        Type type2 = new Type("name2", R.color.darkorchid, false, "assetsName");
        mBill1 = new Bill(type1.getUuid(), account1.getUuid(), now, "name1", new BigDecimal(100), "remark");
        mBill2 = new Bill(type2.getUuid(), account2.getUuid(), now.minusDays(1), "name2", new BigDecimal(200), "remark");

        AppRepository.clearInstance();
        mRepository = AppRepository.getInstance(new SingleExecutors(), mDatabase);
        mDatabase.accountDao().newAccount(account1);
        mDatabase.accountDao().newAccount(account2);
        mDatabase.accountDao().newAccount(ACCOUNT);
        mDatabase.typeDao().newType(type1);
        mDatabase.typeDao().newType(type2);
        mDatabase.typeDao().newType(TYPE);
        mDatabase.billDao().addBill(mBill1);
        mDatabase.billDao().addBill(mBill2);

        Context context = getApplicationContext();
        mRepository.initCurrenciesAndInfos(context);
    }

    @After
    public void tearDown() {
        mDatabase.close();
        AppRepository.clearInstance();
    }

    @Test
    public void getBillInfoList() {
        mRepository.getBillInfoList(now.getYear(), now.getMonthOfYear(), billsInfo -> {
            assertEquals(4, billsInfo.size());
            for (BillInfo billInfo : billsInfo) {
                Log.d(TAG, "getBillInfoList: " + billInfo);
            }
        });
    }

    @Test
    public void getAccount() {
        mRepository.getAccount(mBill1.getAccountId(), this::assertAccount1);
    }

    @Test
    public void getsBills() {
        mRepository.getsBills(now.getYear(), now.getMonthOfYear(), bills ->
                assertEquals(2, bills.size()));
    }

    @Test
    public void getAccounts() {
        mRepository.getAccounts(accounts -> assertEquals(3, accounts.size()));
    }

    @Test
    public void delAccount() {
        mRepository.delAccount(mBill1.getAccountId());
        mRepository.getAccounts(accounts -> assertEquals(2, accounts.size()));
    }

    @Test
    public void changePosition() {
        int[] id = new int[2];
        mRepository.getAccount(mBill1.getAccountId(), a ->
                mRepository.getAccount(mBill2.getAccountId(), b -> {
                    id[0] = a.getId();
                    id[1] = b.getId();
                    mRepository.changePosition(a, b);
                }));
        mRepository.getAccount(mBill1.getAccountId(), a ->
                mRepository.getAccount(mBill2.getAccountId(), b -> {
                    assertEquals(id[1], a.getId());
                    assertEquals(id[0], b.getId());
                }));
    }

    @Test
    public void getBill() {
        mRepository.getBill(mBill1.getUuid(), bill -> assertBill(mBill1, bill));
        mRepository.getBill(mBill2.getUuid(), bill -> assertBill(mBill2, bill));
    }

    @Test
    public void addBill() {
        Bill bill = new Bill(TYPE.getUuid(), ACCOUNT.getUuid(), now, "name", BigDecimal.ZERO, "remark");
        mRepository.addBill(bill);
        mRepository.getBill(bill.getUuid(), b -> assertBill(bill, b));
    }

    @Test
    public void delBill() {
        mRepository.delBill(mBill2.getUuid());
        mRepository.getsBills(now.getYear(), now.getMonthOfYear(), bills ->
                assertEquals(1, bills.size()));
    }

    @Test
    public void updateBill() {
        mBill1.setTypeId(TYPE.getUuid());
        mBill1.setAccountId(ACCOUNT.getUuid());
        mBill1.setBalance(BigDecimal.ONE);
        mBill1.setName("name");
        mBill1.setDate(now.plusDays(1));
        mBill1.setRemark("update");
        mRepository.updateBill(mBill1);
        mRepository.getBill(mBill1.getUuid(), bill -> assertBill(mBill1, bill));
    }

    @Test
    public void clearBill() {
        mRepository.clearBill();
        mRepository.getsBills(now.getYear(), now.getMonthOfYear(), bills ->
                assertEquals(0, bills.size()));
    }

    @Test
    public void getType() {
        mRepository.getType(mBill1.getTypeId(), this::assertType);
    }

    @Test
    public void getTypes() {
        mRepository.getTypes(true, types -> assertEquals(2, types.size()));
    }

    @Test
    public void delType() {
        mRepository.delType(mBill1.getTypeId());
        mRepository.getTypes(true, types ->
                assertEquals(1, types.size()));
    }

    @Test
    public void getBillsAnnualStats() {
        mRepository.getBillsAnnualStats(now.getYear(), billsStats -> {
            assertEquals(12, billsStats.size());
            for (BillStats billStats : billsStats) {
                Log.d(TAG, "getBillsAnnualStats: " + billStats);
            }
        });
    }

    @Test
    public void getBillStats() {
        mRepository.getBillStats(now.minusDays(2), now.plusDays(2), billStats -> {
            assertEquals(100, billStats.getSum().intValue());
            assertEquals(200, billStats.getIncome().intValue());
            assertEquals(100, billStats.getExpense().intValue());
        });
    }

    @Test
    public void getTypesStats() {
        mRepository.getTypesStats(now.minusDays(2), now.plusDays(2), true, typesStats ->
                assertEquals(1, typesStats.size()));
        mRepository.getTypesStats(now.minusDays(2), now.plusDays(2), false, typesStats ->
                assertEquals(1, typesStats.size()));
    }

    @Test
    public void getTypeStats() {
        mRepository.getTypeStats(now.minusDays(2), now.plusDays(2), mBill1.getTypeId(), typeStats ->
                assertEquals(100, typeStats.balance.intValue()));
    }

    @Test
    public void getTypeAverage() {
        mRepository.getTypeStats(now.minusDays(2), now.plusDays(2), mBill1.getTypeId(), typeStats ->
                assertEquals(100, typeStats.balance.intValue()));
    }

    @Test
    public void getAccountStats() {
        mRepository.getAccountStats(mBill1.getAccountId(), now.minusDays(2), now.plusDays(2), accountStats ->
                assertEquals(-100, accountStats.getSum()));
    }

    @Test
    public void getCurrency() {
        String name = "CNY";
        mRepository.getCurrency(name, new CurrencyDataSource.LoadExchangeRateCallback() {
            @Override
            public void onExchangeRateLoaded(Currency currency) {
                Log.d(TAG, "onCurrencyLoaded: " + currency);
                assertEquals(name, currency.getName());
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    @Test
    public void getCurrencyExchangeRate() {
        String from = "HKD";
        String to = "CNY";
        mRepository.getCurrencyExchangeRate(from, to, new CurrencyDataSource.LoadExchangeRateCallback() {
            @Override
            public void onExchangeRateLoaded(Currency currency) {
                Log.d(TAG, "onExchangeRateLoaded: " + currency);
                assertEquals(from, currency.getSource());
                assertEquals(to, currency.getName());
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    @Test
    public void getCurrenciesExchangeRate() {
        String from = "CNY";
        mRepository.getCurrenciesExchangeRate(from, new CurrencyDataSource.LoadExchangeRatesCallback() {
            @Override
            public void onExchangeRatesLoaded(List<Currency> currencies) {
                for (Currency currency : currencies) {
                    assertEquals(from, currency.getSource());
                    Log.d(TAG, "onExchangeRatesLoaded: " + currency);
                }
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    @Test
    public void getFavouriteCurrenciesExchangeRate() {
        String from = "CNY";
        mRepository.getFavouriteCurrenciesExchangeRate(from, new CurrencyDataSource.LoadExchangeRatesCallback() {
            @Override
            public void onExchangeRatesLoaded(List<Currency> currencies) {
                for (Currency currency : currencies) {
                    assertEquals(from, currency.getSource());
                    assertTrue(currency.getFavourite());
                    Log.d(TAG, "onExchangeRatesLoaded: " + currency);
                }
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    @Test
    public void getCurrencyInfo() {
        String name = "CNY";
        mRepository.getCurrencyInfo(name, new CurrencyDataSource.LoadCurrencyInfoCallback() {
            @Override
            public void onCurrencyInfoLoaded(CurrencyInfo info) {
                assertEquals(name, info.getName());
                Log.d(TAG, "onCurrencyInfoLoaded: " + info);
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    @Test
    public void getFavouriteCurrenciesInfo() {
        mRepository.getFavouriteCurrenciesInfo(new CurrencyDataSource.LoadCurrenciesInfoCallback() {
            @Override
            public void onCurrenciesInfoLoaded(List<CurrencyInfo> infos) {
                assertFalse(infos.isEmpty());
                Log.d(TAG, "onCurrenciesInfoLoaded: " + infos);
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    @Test
    public void updateCurrencies() {
        mRepository.updateCurrencies(ApplicationProvider.getApplicationContext(), new CurrencyDataSource.UpdateCallback() {
            @Override
            public void connectFailed(String msg) {
                Log.e(TAG, "connectFailed: " + msg);
                fail();
            }

            @Override
            public void updated() {
                assertTrue(true);
            }
        });
    }
}