package com.example.nickelfoxassignment.stopwatch

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.IBinder
import com.example.nickelfoxassignment.utils.Constants
import com.example.nickelfoxassignment.utils.getStopwatchTime
import java.text.SimpleDateFormat
import java.util.*

class ForegroundService : Service(), CountUpTimer.CountUpListeners {
    private var timer: Timer = Timer()
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent :PendingIntent

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        const val INITIAL_DATA = "00 : 00"
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, StopwatchActivity::class.java).apply {
                this.flags = FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        if (intent.getBooleanExtra(Constants.RUNNING, false)) {
            val millis = Constants.SECONDS.value
            timer = CountUpTimer(millis ?: 0, 100L, this)
        } else {
            if (Constants.IS_RESET.value == true) {
                Constants.SECONDS.postValue(0)
                //Constants.DATA.postValue(INITIAL_DATA)
                stopForeground(true)
                removeNotification(this@ForegroundService)
            }
            timer.cancel()
        }
        startForeground(
            1,
            NotificationBuilder.getNotificationBuilder(this,pendingIntent, INITIAL_DATA).build()
        )
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
        //Constants.DATA.postValue(timeString)
        notificationManager.notify(
            1,
            NotificationBuilder.getNotificationBuilder(this,pendingIntent, l.getStopwatchTime()).build()
        )
    }
}
