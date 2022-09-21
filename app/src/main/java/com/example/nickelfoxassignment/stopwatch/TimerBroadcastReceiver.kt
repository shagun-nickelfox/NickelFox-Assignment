package com.example.nickelfoxassignment.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.nickelfoxassignment.Constants

class TimerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action.equals(Constants.PAUSE)) {
            startServiceWitBundle(context)
            val i = Intent(Constants.BROADCAST_NAME)
            i.putExtra(Constants.PLAY, true)
            i.putExtra(Constants.PAUSE, false)
            context.sendBroadcast(i)
        } else if (intent.action.equals(Constants.RESET)) {
            Constants.IS_RESET.value = true
            startServiceWitBundle(context)
            val i = Intent(Constants.BROADCAST_NAME)
            i.putExtra(Constants.PLAY, true)
            i.putExtra(Constants.PAUSE, false)
            context.sendBroadcast(i)
        } else {
            val i = Intent(Constants.BROADCAST_NAME)
            i.putExtra(Constants.LAP, Constants.DATA.value)
            context.sendBroadcast(i)
        }
    }

    private fun startServiceWitBundle(context: Context) {
        val bundle = Bundle()
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