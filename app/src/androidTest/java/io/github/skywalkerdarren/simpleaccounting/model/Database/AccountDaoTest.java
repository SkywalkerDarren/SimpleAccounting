package io.github.skywalkerdarren.simpleaccounting.model.Database;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.LogUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;

import static io.github.skywalkerdarren.simpleaccounting.model.entity.Account.PNG;

@RunWith(AndroidJUnit4.class)
public class AccountDaoTest {

    Account mAccount = new Account(UUID.randomUUID(), "现金", "现金金额", BigDecimal.ZERO, "cash" + PNG, R.color.amber500);

    private AccountDatabase mDatabase;

    @Before
    public void setUp() throws Exception {
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AccountDatabase.class).build();
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }

    @Test
    public void insertAndGetAccount() {
        LogUtil.logDebug("test", "test" + mAccount);
        System.out.println("asdfasdf");
        mDatabase.accountDao().insertAccount(mAccount);
        List<Account> accounts = mDatabase.accountDao().getAccounts();
        LogUtil.logDebug("test", "test" + accounts.size());
    }
}