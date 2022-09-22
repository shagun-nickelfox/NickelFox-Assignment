package com.example.nickelfoxassignment.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.nickelfoxassignment.utils.Constants
import com.example.nickelfoxassignment.utils.NotificationActions

class TimerBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "TimerBroadcastReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: action: ${intent.action}")
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
                i.putExtra(NotificationActions.RESET.name, true)
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
                NotificationActions.PLAY,
                NotificationActions.PAUSE,
                NotificationActions.RESET -> putExtra(action.name, true)
                else -> Unit
            }
        })
    }
}