package com.example.nickelfoxassignment.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.nickelfoxassignment.utils.Constants
import com.example.nickelfoxassignment.utils.NotificationActions

class TimerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action.equals(NotificationActions.PAUSE.name)) {
            startServiceWitBundle(context, NotificationActions.PAUSE)
            val i = Intent(Constants.BROADCAST_NAME)
            i.putExtra(NotificationActions.PLAY.name, true)
            i.putExtra(NotificationActions.PAUSE.name, false)
            context.sendBroadcast(i)
        } else if (intent.action.equals(NotificationActions.RESET.name)) {
            Constants.IS_RESET.value = true
            startServiceWitBundle(context, NotificationActions.RESET)
            val i = Intent(Constants.BROADCAST_NAME)
            i.putExtra(NotificationActions.PLAY.name, true)
            i.putExtra(NotificationActions.PAUSE.name, false)
            context.sendBroadcast(i)
        } else if (intent.action.equals(NotificationActions.PLAY.name)) {
            startServiceWitBundle(context, NotificationActions.PLAY)
            val i = Intent(Constants.BROADCAST_NAME)
            i.putExtra(NotificationActions.PLAY.name, false)
            i.putExtra(NotificationActions.PAUSE.name, true)
            context.sendBroadcast(i)
        } else {
            val i = Intent(Constants.BROADCAST_NAME)
            i.putExtra(NotificationActions.LAP.name, true)
            context.sendBroadcast(i)
        }
    }

    private fun startServiceWitBundle(context: Context, action: NotificationActions) {
        val bundle = Bundle()
        bundle.putString("Action", "")
        if (action == NotificationActions.PLAY)
            bundle.putBoolean(Constants.RUNNING, true)
        else
            bundle.putBoolean(Constants.RUNNING, false)
        val intent = Intent(
            context,
            ForegroundService::class.java
        ).apply {
            putExtras(bundle)
        }
        context.startService(intent)
    }
}