package com.udacity

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

private val REQUEST_CODE = 0
private val FLAGS = 0
private const val CHANNEL_ID = "channelId"
private const val CHANNEL_NAME = "Downloads"

fun NotificationManager.sendNotification(
        title: String,
        messageBody: String,
        applicationContext: Context,
        pendingIntent: PendingIntent,
        notificationId: Int
) {
    // check for existing notification channel on versions O and above
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        if (!isNotificationChannelAvailable(this)) {
            createNotificationChannel(this)
        }
    }

    val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
//            .addAction(R.drawable.ic_baseline_cloud_download_24, "Check the status", contentPendingIntent)
            .addAction(
                    NotificationCompat.Action.Builder(
                            0, context.getString(R.string.check_the_status), pendingIntent).build()
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(notificationId, notificationBuilder.build())

}

@TargetApi(Build.VERSION_CODES.O)
fun isNotificationChannelAvailable(notificationManager: NotificationManager) =
        notificationManager.getNotificationChannel(CHANNEL_ID) != null

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(notificationManager: NotificationManager) {
    val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
    notificationManager.createNotificationChannel(channel)
}