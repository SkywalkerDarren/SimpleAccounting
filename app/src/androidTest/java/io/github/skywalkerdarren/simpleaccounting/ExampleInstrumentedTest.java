package io.github.skywalkerdarren.simpleaccounting;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        BillLab lab = BillLab.getInstance(appContext);
        DateTime dateTime = DateTime.now();
        int y = dateTime.getYear();
        int m = dateTime.monthOfYear().get();
        int d = dateTime.dayOfMonth().get();
        List<Bill> bills = lab.getsBills(y, m);
        for (Bill bill : bills) {
            Log.d("test", bill.getName() + "\t" +
                    (bill.isExpense() ? bill.getBalance().negate() : bill.getBalance()) + "\t" +
                    bill.getDate() + "\t" +
                    bill.getType() + "\t" +
                    bill.getRemark());
        }

        Log.d("test", "useAppContext: " + lab.getStatics(y, m, d).get("income"));

        assertEquals("io.github.skywalkerdarren.simpleaccounting", appContext.getPackageName());
    }
}
