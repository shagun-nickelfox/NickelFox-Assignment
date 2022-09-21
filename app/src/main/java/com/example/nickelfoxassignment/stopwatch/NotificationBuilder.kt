package com.example.nickelfoxassignment.stopwatch

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.app.NotificationCompat
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R

object NotificationBuilder {
    fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
        val intent = Intent(context, StopwatchActivity::class.java).apply {
            flags = FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(context, Constants.CHANNEL_ID).apply {
            setContentTitle(TIME_ELAPSED)
            setContentText("${Constants.DATA.value}")
            setOnlyAlertOnce(true)
            setContentIntent(pendingIntent)
            priority = NotificationCompat.PRIORITY_HIGH
            addAction(createAction(Constants.PAUSE, context))
            addAction(createAction(Constants.RESET, context))
            addAction(createAction(Constants.LAP, context))
            setSmallIcon(R.drawable.ic_launcher_foreground)
        }
    }

    private fun createAction(action: String, context: Context): NotificationCompat.Action {
        return NotificationCompat.Action(
            R.drawable.cancel,
            action,
            createPendingIntent(action, context)
        )
    }

    private fun createPendingIntent(action: String, context: Context): PendingIntent {
        return PendingIntent.getBroadcast(
            context, 0,
            Intent(context, TimerBroadcastReceiver::class.java)
                .setAction(action),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private const val TIME_ELAPSED = "Time Elapsed"
}