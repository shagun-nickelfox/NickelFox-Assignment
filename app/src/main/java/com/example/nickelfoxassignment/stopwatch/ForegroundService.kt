package com.example.nickelfoxassignment.stopwatch

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.IBinder
import com.example.nickelfoxassignment.utils.Constants
import com.example.nickelfoxassignment.utils.NotificationActions
import com.example.nickelfoxassignment.utils.getStopwatchTime
import java.util.*

class ForegroundService : Service(), CountUpTimer.CountUpListeners {
    private var timer: Timer = Timer()
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private var notifyHandler = 0L


    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val play = intent.getBooleanExtra(NotificationActions.PLAY.name, false)
        val pause = intent.getBooleanExtra(NotificationActions.PAUSE.name, false)
        val reset = intent.getBooleanExtra(NotificationActions.RESET.name, false)

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, StopwatchActivity::class.java).apply {
                this.flags = FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (play) {
            val millis = Constants.SECONDS.value
            timer = CountUpTimer(millis ?: 0, 100L, this)
            startForeground(
                1,
                NotificationBuilder.getNotificationBuilder(
                    this,
                    pendingIntent,
                    (millis ?: 0).getStopwatchTime(Constants.PATTERN_MIN_SEC),
                    true
                ).build()
            )
        } else {
            timer.cancel()
            if (pause)
                notificationManager.notify(
                    1,
                    NotificationBuilder.getNotificationBuilder(
                        this,
                        pendingIntent,
                        (Constants.SECONDS.value ?: 0).getStopwatchTime(Constants.PATTERN_MIN_SEC),
                        false
                    ).build()
                )
            else if (reset) {
                Constants.SECONDS.postValue(0)
                stopForeground(true)
                removeNotification(this@ForegroundService)
            }
        }
        return START_STICKY
    }

    private fun removeNotification(ctx: Context) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        ctx.stopService(Intent(ctx, ForegroundService::class.java))
    }

    override fun onTick(l: Long) {
        Constants.SECONDS.postValue(l)
        if (l - 1000 == notifyHandler) {
            notifyHandler = l
            notificationManager.notify(
                1,
                NotificationBuilder.getNotificationBuilder(
                    applicationContext,
                    pendingIntent,
                    l.getStopwatchTime(Constants.PATTERN_MIN_SEC),
                    true
                ).build()
            )
        }
    }
}
