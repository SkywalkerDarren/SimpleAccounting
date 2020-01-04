package io.github.skywalkerdarren.simpleaccounting.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.io.Serializable
import java.util.*

object AlarmTimerUtil {
    fun setAlarmTimer(context: Context, alarmId: Int, calendar: Calendar, action: String, map: Map<String, Serializable>) {
        val myIntent = Intent()
        myIntent.action = action
        for (key in map.keys) {
            myIntent.putExtra(key, map[key])
        }

        //如果是广播，就这么写
//        val senderBroadcast = PendingIntent.getBroadcast(context, alarmId, myIntent, 0)
        val senderService = PendingIntent.getService(context, alarmId, myIntent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                senderService)
    }

    fun cancelAlarmTimer(context: Context, action: String?, alarmId: Int) {
        val myIntent = Intent()
        myIntent.action = action
        //如果是广播，就这么写
//        val senderBroadcast = PendingIntent.getBroadcast(context, alarmId, myIntent, 0)
        val senderService = PendingIntent.getService(context, alarmId, myIntent, 0)
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(senderService)
    }
}
