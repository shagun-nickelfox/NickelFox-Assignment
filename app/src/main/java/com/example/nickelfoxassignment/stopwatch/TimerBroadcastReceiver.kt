package com.example.nickelfoxassignment.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.nickelfoxassignment.utils.Constants
import com.example.nickelfoxassignment.utils.NotificationActions

class TimerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            NotificationActions.PAUSE.name -> {
                startServiceWitBundle(context, NotificationActions.PAUSE)
                val i = Intent(Constants.BROADCAST_NAME)
                i.putExtra(NotificationActions.PLAY.name, true)
                i.putExtra(NotificationActions.PAUSE.name, false)
                context.sendBroadcast(i)
            }
            NotificationActions.RESET.name -> {
                startServiceWitBundle(context, NotificationActions.RESET)
                val i = Intent(Constants.BROADCAST_NAME)
                i.putExtra(NotificationActions.PLAY.name, true)
                i.putExtra(NotificationActions.PAUSE.name, false)
                context.sendBroadcast(i)
            }
            NotificationActions.PLAY.name -> {
                startServiceWitBundle(context, NotificationActions.PLAY)
                val i = Intent(Constants.BROADCAST_NAME)
                i.putExtra(NotificationActions.PLAY.name, false)
                i.putExtra(NotificationActions.PAUSE.name, true)
                context.sendBroadcast(i)
            }
            NotificationActions.LAP.name -> {
                val i = Intent(Constants.BROADCAST_NAME)
                i.putExtra(NotificationActions.LAP.name, true)
                context.sendBroadcast(i)
            }
        }
    }

    private fun startServiceWitBundle(context: Context, action: NotificationActions) {
        context.startService(Intent(
            context,
            ForegroundService::class.java
        ).apply {
            when (action) {
                NotificationActions.PLAY -> putExtra(NotificationActions.PLAY.name, false)
                NotificationActions.PAUSE -> putExtra(NotificationActions.PAUSE.name, false)
                NotificationActions.RESET -> putExtra(NotificationActions.RESET.name, false)
                else -> Unit
            }
        })
    }
}