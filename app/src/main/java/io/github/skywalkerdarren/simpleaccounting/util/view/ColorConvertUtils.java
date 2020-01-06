package io.github.skywalkerdarren.simpleaccounting.util.view;

import android.content.Context;

import androidx.annotation.ColorRes;

public final class ColorConvertUtils {
    public static int convertIdToColor(Context context, @ColorRes int colorId) {
        return context.getResources().getColor(colorId);
    }
}
