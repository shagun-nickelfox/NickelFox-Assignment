package com.example.nickelfoxassignment.newsapp.connectivity

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object MonitorConnectivity {
    private val IMPL =
        ConnectionCheck

    fun isConnected(connectivityManager: ConnectivityManager): Boolean {
        return IMPL.isConnected(connectivityManager)
    }
}

interface ConnectedCompat {
    fun isConnected(connectivityManager: ConnectivityManager): Boolean
}

object ConnectionCheck : ConnectedCompat {
    override fun isConnected(connectivityManager: ConnectivityManager): Boolean {
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) == true
    }
}