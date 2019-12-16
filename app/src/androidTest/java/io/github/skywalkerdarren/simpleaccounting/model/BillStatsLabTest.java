package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author darren
 * @date 2018/4/8
 */
public class BillStatsLabTest {
    private StatsLab mStatsLab;
    private Context mContext;
    private static final String TAG = "BillStatsLabTest";

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        mStatsLab = StatsLab.getInstance(mContext);
    }

    @Test
    public void getAnnualStats() throws Exception {
    }

    @Test
    public void getMonthStats() throws Exception {
    }

    @Test
    public void getTypeStats() throws Exception {
        List<StatsLab.TypeStats> stats = mStatsLab.getTypeStats(DateTime.now().minusYears(1), DateTime.now(), false);
        for (StatsLab.TypeStats typeStats : stats) {
            Log.d(TAG, "getTypeStats: " + typeStats.getType().getName());
            Log.d(TAG, "getTypeStats: " + typeStats.getSum());
        }
    }

    @Test
    public void getAccountStats() throws Exception {
        Account account = AccountLab.getInstance(mContext).getAccounts().get(0);
        List<StatsLab.AccountStats> stats = mStatsLab.getAccountStats(account.getId(), 2018);
    }

    @Test
    public void getStats() throws Exception {
    }

}