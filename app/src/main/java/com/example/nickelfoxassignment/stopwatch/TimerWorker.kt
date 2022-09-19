package com.example.nickelfoxassignment.stopwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R
import java.util.*

class TimerWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val running = inputData.getBoolean(Constants.RUNNING, false)
    var secs = Constants.SECONDS.value
    private var timerTask: TimerTask? = null
    val notificationManager = createNotificationChannel(context)
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
    val intent = Intent(context, StopwatchActivity::class.java)
    private lateinit var pendingIntent: PendingIntent

    override fun doWork(): Result {
        intent.flags = FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP
        pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
        timer?.cancel()
        timer = Timer()
        if (running) {
            timerTask = object : TimerTask() {
                override fun run() {
                    secs = secs!! + 1
                    Constants.DATA.postValue(getTimerText())
                    notificationManager.notify(
                        1, builder.setContentTitle(TIME_ELAPSED)
                            .setContentText("${Constants.DATA.value}")
                            .setAutoCancel(false)
                            .setOngoing(true)
                            .setOnlyAlertOnce(true)
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.ic_launcher_foreground).build()
                    )
                }
            }
            timer!!.scheduleAtFixedRate(timerTask, 0, 1)
        } else {
            if (Constants.IS_RESET.value!!) {
                Constants.SECONDS.postValue(0)
                Constants.DATA.postValue(INITIAL_TIME)
            }
            notificationManager.cancelAll()
            timer?.cancel()
            timer = null
        }
        return Result.success()
    }


    private fun getTimerText(): String {
        val minutes = (secs!! / 1000) / 60
        val seconds = ((secs!! / 1000) % 60)
        Constants.SECONDS.postValue(secs!!)
        return formatTime(seconds, minutes, secs!!)
    }

    private fun createNotificationChannel(context: Context): NotificationManager {
        val name = NOTIFICATION_NAME
        val descriptionText = NOTIFICATION_DESC
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return notificationManager
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

    companion object {
        var timer: Timer? = null
        const val CHANNEL_ID = "notify"
        const val TIME_ELAPSED = "Time Elapsed"
        const val NOTIFICATION_NAME = "timer_notification"
        const val NOTIFICATION_DESC = "description"
        const val INITIAL_TIME = "00 : 00"
    }
}