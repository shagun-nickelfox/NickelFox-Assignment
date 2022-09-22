package com.example.nickelfoxassignment.stopwatch

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.nickelfoxassignment.utils.Constants
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.utils.NotificationActions

object NotificationBuilder {
    fun getNotificationBuilder(
        context: Context,
        pi: PendingIntent,
        time: String,
        running: Boolean
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, Constants.CHANNEL_ID).apply {
            setContentText(time)
            setOnlyAlertOnce(true)
            setContentIntent(pi)
            priority = NotificationCompat.PRIORITY_HIGH
            if (running) {
                addAction(createAction(NotificationActions.PAUSE.name, context))
                addAction(createAction(NotificationActions.LAP.name, context))
            } else {
                addAction(createAction(NotificationActions.PLAY.name, context))
                addAction(createAction(NotificationActions.RESET.name, context))
            }
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
}