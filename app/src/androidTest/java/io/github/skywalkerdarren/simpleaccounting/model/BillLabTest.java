package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.util.List;

/**
 * Created by darren on 2018/3/15.
 */
public class BillLabTest {
    @Test
    public void getMonthStats() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        List<StatsLab.BillStats> statsList = StatsLab.getInstance(appContext).getMonthStats(2018, 3);
        for (StatsLab.BillStats stats : statsList) {
            System.out.println(stats.getSum().toString());
        }
    }

}