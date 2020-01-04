package io.github.skywalkerdarren.simpleaccounting.util

import android.content.Context

object PreferenceUtil {
    const val CURRENT_HOUR = "CURRENT_HOUR"
    const val BUDGET = "BUDGET"
    const val LAST_UPDATE_TIMESTAMP = "LAST_UPDATE_TIMESTAMP"
    const val RUN_APP_TIMES = "RUN_APP_TIMES"
    const val CUMULATIVE_DAYS = "CUMULATIVE_DAYS"
    const val LAST_RUN_DATE = "LAST_RUN_DATE"
    const val IS_PUSH = "IS_PUSH"
    private const val MAIN = "MAIN"
    /**
     * get string by key, if key does not exist return null
     */
    @JvmStatic
    fun getString(context: Context, key: String): String? {
        val preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE)
        return preferences.getString(key, null)
    }

    @JvmStatic
    fun getString(context: Context, key: String, defValue: String): String {
        val preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE)
        return preferences.getString(key, defValue).toString()
    }

    @JvmStatic
    fun setString(context: Context, key: String, value: String?) {
        val preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE)
        preferences.edit().putString(key, value).apply()
    }

    @JvmStatic
    fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        val preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE)
        return preferences.getBoolean(key, defValue)
    }

    @JvmStatic
    fun setBoolean(context: Context, key: String, value: Boolean) {
        val preferences = context.getSharedPreferences(MAIN, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(key, value).apply()
    }
}