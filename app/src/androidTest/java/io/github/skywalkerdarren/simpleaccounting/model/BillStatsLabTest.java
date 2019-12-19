package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;

/**
 * @author darren
 * @date 2018/4/8
 */
public class BillStatsLabTest {
    private Context mContext;
    private static final String TAG = "BillStatsLabTest";
    private AppRepositry mRepositry;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        mRepositry = AppRepositry.getInstance(mContext);
    }

    @Test
    public void getAnnualStats() throws Exception {
    }

    @Test
    public void getMonthStats() throws Exception {
    }

    @Test
    public void getTypeStats() throws Exception {
        List<TypeStats> stats = mRepositry.getTypesStats(DateTime.now().minusYears(1), DateTime.now(), false);
        for (TypeStats typeStats : stats) {
            Type type = mRepositry.getType(typeStats.getTypeId());
            Log.d(TAG, "getTypeStats: " + type.getName());
            Log.d(TAG, "getTypeStats: " + typeStats.getBalance());
        }
    }

    @Test
    public void getAccountStats() throws Exception {
        Account account = AppRepositry.getInstance(mContext).getAccounts().get(0);
        List<AccountStats> stats = mRepositry.getAccountStats(account.getUUID(), 2018);
    }

    @Test
    public void getStats() throws Exception {
    }

}