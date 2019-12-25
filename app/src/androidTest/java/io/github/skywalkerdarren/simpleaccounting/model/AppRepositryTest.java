package io.github.skywalkerdarren.simpleaccounting.model;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.Matchers.is;
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
}