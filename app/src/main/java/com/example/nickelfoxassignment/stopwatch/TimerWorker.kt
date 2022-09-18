package com.example.nickelfoxassignment.stopwatch

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
    private val running = inputData.getBoolean("running", false)
    var secs = Constants.SECONDS.value
    private var timerTask: TimerTask? = null
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val builder = NotificationCompat.Builder(context, "notify")
    val intent = Intent(context, StopwatchActivity::class.java)
    private lateinit var pendingIntent: PendingIntent

    companion object {
        var timer: Timer? = null
    }

    override fun doWork(): Result {
        intent.flags = FLAG_ACTIVITY_REORDER_TO_FRONT
        pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        timer?.cancel()
        timer = Timer()
        if (running) {
            timerTask = object : TimerTask() {
                override fun run() {
                    secs = secs!! + 1
                    Constants.DATA.postValue(getTimerText())
                    notificationManager.notify(
                        1, builder.setContentTitle("Time Elapsed")
                            .setContentText("${Constants.DATA.value}")
                            .setAutoCancel(false)
                            .setOngoing(true)
                            .setOnlyAlertOnce(true)
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.ic_launcher_foreground).build()
                    )
                }
            }
            timer!!.scheduleAtFixedRate(timerTask, 0, 1000)
        } else {
            notificationManager.cancel(1)
            timer?.cancel()
            timer = null
        }
        return Result.success()
    }

    private fun getTimerText(): String {
        val seconds = secs!! % 86400 % 3600 % 60
        val minutes = secs!! % 86400 % 3600 / 60
        val hours = secs!! % 86400 / 3600
        Constants.SECONDS.postValue(seconds)
        return formatTime(seconds, minutes, hours)
    }

    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format(
            "%02d",
            minutes
        ) + " : " + String.format("%02d", seconds)
    }
}