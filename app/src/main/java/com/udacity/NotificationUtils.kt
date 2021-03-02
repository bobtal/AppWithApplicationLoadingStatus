package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0
private const val CHANNEL_ID = "channelId"

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.download_complete))
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
//            .addAction(R.drawable.ic_baseline_cloud_download_24, "Check the status", contentPendingIntent)
            .addAction(
                    NotificationCompat.Action.Builder(
                    0, "Check the status", contentPendingIntent).build()
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(NOTIFICATION_ID, notificationBuilder.build())

}