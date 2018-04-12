package io.github.skywalkerdarren.simpleaccounting.util;


import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author darren
 * @date 2018/4/12
 */

public class FormatUtil {
    public static String getNumberic(BigDecimal bigDecimal) {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return format.format(bigDecimal);
    }
}
