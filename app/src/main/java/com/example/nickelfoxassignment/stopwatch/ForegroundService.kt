package com.example.nickelfoxassignment.stopwatch

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.nickelfoxassignment.Constants
import java.util.*

class ForegroundService : Service() {
    private var secs: Long? = 0
    private var timerTask: TimerTask? = null

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        secs = Constants.SECONDS.value
        timer?.cancel()
        timer = Timer()
        if (intent!!.getBooleanExtra(Constants.RUNNING, false)) {
            timerTask = object : TimerTask() {
                override fun run() {
                    secs = secs!! + 1
                    Constants.DATA.postValue(getTimerText())
                    updateNotification(context = this@ForegroundService)
                }
            }
            timer!!.scheduleAtFixedRate(timerTask, 0, 1)
        } else {
            if (Constants.IS_RESET.value!!) {
                Constants.SECONDS.postValue(0)
                Constants.DATA.postValue("00 : 00")
            }
            timer?.cancel()
            timer = null
            removeNotification(this@ForegroundService)
        }
        startForeground(
            1,
            NotificationBuilder.getNotificationBuilder(applicationContext).build()
        )
        return START_STICKY
    }

    override fun stopService(intent: Intent?): Boolean {
        return super.stopService(intent)
    }

    private fun getTimerText(): String {
        val minutes = (secs!! / 1000) / 60
        val seconds = ((secs!! / 1000) % 60)
        Constants.SECONDS.postValue(secs!!)
        return formatTime(seconds, minutes, secs!!)
    }

    private fun formatTime(seconds: Long, minutes: Long, millis: Long): String {
        return String.format(
            "%02d",
            minutes
        ) + " : " + String.format("%02d", seconds) + "." + String.format(
            "%02d",
            (millis % 100)
        )
    }

    private fun updateNotification(context: Context) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, NotificationBuilder.getNotificationBuilder(context).build())
    }

    private fun removeNotification(ctx: Context) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        ctx.stopService(Intent(ctx, ForegroundService::class.java))
    }

    companion object {
        var timer: Timer? = null
    }
}