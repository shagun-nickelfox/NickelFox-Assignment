package com.example.nickelfoxassignment.stopwatch

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nickelfoxassignment.Constants
import java.util.*

class TimerWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val running = inputData.getBoolean("running", false)
    var secs = Constants.SECONDS.value
    private var timerTask: TimerTask? = null

    companion object {
        var timer: Timer? = null
    }

    override fun doWork(): Result {
        //Utils.sendNotification(applicationContext)
        timer?.cancel()
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                secs = secs!! + 1
                Constants.DATA.postValue(getTimerText())
            }
        }
        timer!!.scheduleAtFixedRate(timerTask, 0, 1000)
        if (!running) {
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