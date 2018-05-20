package io.github.skywalkerdarren.simpleaccounting.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

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

    public static Bitmap idToBitmap(Context context, @DrawableRes int res) {
        return BitmapFactory.decodeResource(context.getResources(), res);
    }
}
