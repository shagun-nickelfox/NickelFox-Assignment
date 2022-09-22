package com.example.nickelfoxassignment.stopwatch

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.nickelfoxassignment.Constants
import java.text.SimpleDateFormat
import java.util.*

class ForegroundService : Service(), CountUpTimer.CountUpListeners {
    private var timer: Timer = Timer()
    private val sdf = SimpleDateFormat(PATTERN, Locale.getDefault())
    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        const val PATTERN = "mm:ss.SS"
        const val TIME_ZONE = "GMT"
        const val INITIAL_DATA = "00 : 00"
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        sdf.timeZone = TimeZone.getTimeZone(TIME_ZONE)

        if (intent.getBooleanExtra(Constants.RUNNING, false)) {
            val millis = Constants.SECONDS.value
            timer = CountUpTimer(millis ?: 0, 100L, this)
        } else {
            if (Constants.IS_RESET.value == true) {
                Constants.SECONDS.postValue(0)
                Constants.DATA.postValue(INITIAL_DATA)
            }
            timer.cancel()
            stopForeground(true)
            removeNotification(this@ForegroundService)
        }
        startForeground(
            1,
            NotificationBuilder.getNotificationBuilder(applicationContext).build()
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
        val timeString = sdf.format(Date(l))
        Constants.DATA.postValue(timeString)
        notificationManager.notify(
            1,
            NotificationBuilder.getNotificationBuilder(applicationContext).build()
        )
    }
}
