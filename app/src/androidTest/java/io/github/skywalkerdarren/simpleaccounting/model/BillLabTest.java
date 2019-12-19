package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;

/**
 * Created by darren on 2018/3/15.
 */
public class BillLabTest {
    @Test
    public void getMonthStats() throws Exception {
        Context appContext = ApplicationProvider.getApplicationContext();
        List<BillStats> statsList = AppRepositry.getInstance(appContext).getMonthStats(2018, 3);
        for (BillStats stats : statsList) {
            System.out.println(stats.getSum().toString());
        }
    }

}