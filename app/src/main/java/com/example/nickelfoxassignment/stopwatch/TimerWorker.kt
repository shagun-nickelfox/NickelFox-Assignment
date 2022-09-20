package com.example.nickelfoxassignment.stopwatch

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nickelfoxassignment.Constants

class TimerWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val running = inputData.getBoolean(Constants.RUNNING, false)

    override fun doWork(): Result {
        val intent = Intent(applicationContext, ForegroundService::class.java)
        intent.putExtra(Constants.RUNNING, running)
        applicationContext.startService(intent)
        return Result.success()
    }
}