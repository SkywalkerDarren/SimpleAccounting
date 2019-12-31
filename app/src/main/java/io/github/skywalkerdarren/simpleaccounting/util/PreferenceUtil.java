package io.github.skywalkerdarren.simpleaccounting.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public static final String BUDGET = "BUDGET";
    public static final String LAST_UPDATE_TIMESTAMP = "LAST_UPDATE_TIMESTAMP";
    public static final String RUN_APP_TIMES = "RUN_APP_TIMES";
    public static final String CUMULATIVE_DAYS = "CUMULATIVE_DAYS";
    public static final String LAST_RUN_DATE = "LAST_RUN_DATE";
    private static String MAIN = "MAIN";


    private PreferenceUtil() {
    }

    /**
     * get string by key, if key does not exist return null
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE);
        return preferences.getString(key, defValue);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

}
