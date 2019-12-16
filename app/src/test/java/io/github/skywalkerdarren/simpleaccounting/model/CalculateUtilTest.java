package io.github.skywalkerdarren.simpleaccounting.model;

import org.junit.Test;

import io.github.skywalkerdarren.simpleaccounting.util.CalculateUtil;

/**
 * Created by darren on 2018/3/12.
 */
public class CalculateUtilTest {
    @Test
    public void getResult() throws Exception {
        String r = new CalculateUtil().getResult("3/3.0");
        System.out.println(r);
    }

}