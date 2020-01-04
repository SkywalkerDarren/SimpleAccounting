package io.github.skywalkerdarren.simpleaccounting.util

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.work.*
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    companion object {
        private const val TAG = "NotificationWorker"
        val worker: PeriodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS).build()
        private const val name = "NotificationWorker"

        @JvmStatic
        fun start(context: Context) = WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(name, ExistingPeriodicWorkPolicy.KEEP, worker)

        @JvmStatic
        fun cancel(context: Context) = WorkManager.getInstance(context)
                .cancelUniqueWork(name)
    }

    private val isPush = PreferenceUtil.getBoolean(context, PreferenceUtil.IS_PUSH, false)

    override fun doWork(): Result {
        Log.d(TAG, "alive")
        if (hourChange()) {
            // 时段变化 重置flag
            PreferenceUtil.setBoolean(applicationContext, PreferenceUtil.IS_PUSH, false)
        }
        val hourList: MutableList<String> = applicationContext.resources
                .getStringArray(R.array.list_time_def_value).toMutableList()
        if (compareCurrentHour(hourList)) {
            if (!isPush) {
                //如果在指定时间段，并且没有推送过通知
                PreferenceUtil.getBoolean(applicationContext, PreferenceUtil.IS_PUSH, true)
                //继续后面的推送通知代码
            } else {
                //在指定时间段，已推送过了，则不再推送
                return Result.retry()
            }
        } else {
            //不在时间段，重置标志位false
            PreferenceUtil.setBoolean(applicationContext, PreferenceUtil.IS_PUSH, false)
            return Result.retry()
        }

        val resultIntent = BillEditActivity.newIntent(applicationContext, null, 0, 0)
        val intent = PendingIntent.getActivity(applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //推送通知
        NotificationUtil.makeNotification(applicationContext, "记账提醒小助手", "是不是该记上一笔了？", "accounting_reminder", intent)

        return Result.success()
    }

    private fun hourChange(): Boolean {
        val hour = PreferenceUtil.getString(applicationContext, PreferenceUtil.CURRENT_HOUR, "")
        val current = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return current.toString() != hour
    }

    private fun compareCurrentHour(targetHour: List<String>): Boolean {
        val current = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        PreferenceUtil.setString(applicationContext, PreferenceUtil.CURRENT_HOUR, current.toString())
        return targetHour.contains(current.toString())
    }

}