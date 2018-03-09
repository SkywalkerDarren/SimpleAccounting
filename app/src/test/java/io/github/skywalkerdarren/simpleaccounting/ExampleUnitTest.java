package io.github.skywalkerdarren.simpleaccounting;

import org.junit.Test;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println("start");

        Bill bill = new Bill();
        System.out.println("resId" + bill.getTypeResId());
    }
}