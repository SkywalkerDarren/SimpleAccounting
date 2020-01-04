package io.github.skywalkerdarren.simpleaccounting.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.github.skywalkerdarren.simpleaccounting.R
import java.math.BigInteger
import java.security.MessageDigest


object NotificationUtil {
    fun makeNotification(applicationContext: Context, title: String, msg: String, channelId: String, intent: PendingIntent) {
        val notification: Notification = NotificationCompat.Builder(applicationContext, channelId).apply {
            setSmallIcon(R.drawable.ic_logo)
            setContentTitle(title)
            setContentText(msg)
            setContentIntent(intent)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
        }.build()
        val md5 = MessageDigest.getInstance("MD5").apply {
            update(channelId.toByteArray())
        }.digest()
        val id = BigInteger(1, md5).toInt()
        with(NotificationManagerCompat.from(applicationContext)) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT).apply {
                        enableVibration(true)
                        enableLights(true)
                    }
                    createNotificationChannel(channel)
                }
            }
            notify(id, notification)
        }
    }
}