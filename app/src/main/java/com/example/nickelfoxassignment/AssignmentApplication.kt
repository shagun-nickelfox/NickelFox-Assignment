package com.example.nickelfoxassignment

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.nickelfoxassignment.utils.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AssignmentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        WorkManager.initialize(this, Configuration.Builder().build())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.CHANNEL_ID,
            Constants.NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = Constants.NOTIFICATION_DESC
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}