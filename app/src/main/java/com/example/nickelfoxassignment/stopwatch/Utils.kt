package com.example.nickelfoxassignment.stopwatch

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R

object Utils {
    private const val CHANNEL_ID = "notify"
    private const val CHANNEL_NAME = "workmanager-reminder"

    fun sendNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel1.enableVibration(true)
            channel1.enableLights(true)
            channel1.lightColor = R.color.light_green
            channel1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(channel1)
        }
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Time Elapsed")
            .setContentText("${Constants.DATA.value}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationManager.notify(1, builder.build())
    }
}