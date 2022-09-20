package com.example.nickelfoxassignment.stopwatch

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.app.NotificationCompat
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R

object NotificationBuilder {
    fun getNotification(context: Context): Notification {
        val intent = Intent(context, StopwatchActivity::class.java).apply {
            flags = FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(context, NOTIFY).apply {
            setContentTitle(TIME_ELAPSED)
            setContentText("${Constants.DATA.value}")
            setOnlyAlertOnce(true)
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_launcher_foreground)
        }.build()
    }

    private const val NOTIFY = "notify"
    private const val TIME_ELAPSED = "Time Elapsed"
}