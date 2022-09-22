package com.example.nickelfoxassignment.stopwatch

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nickelfoxassignment.utils.NotificationActions

class TimerWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val play = inputData.getBoolean(NotificationActions.PLAY.name, false)
    private val pause = inputData.getBoolean(NotificationActions.PAUSE.name, false)
    private val reset = inputData.getBoolean(NotificationActions.RESET.name, false)

    override fun doWork(): Result {
        applicationContext.startService(
            Intent(
                applicationContext,
                ForegroundService::class.java
            ).putExtra(NotificationActions.PLAY.name, play)
                .putExtra(NotificationActions.PAUSE.name, pause)
                .putExtra(NotificationActions.RESET.name, reset)
        )
        return Result.success()
    }
}