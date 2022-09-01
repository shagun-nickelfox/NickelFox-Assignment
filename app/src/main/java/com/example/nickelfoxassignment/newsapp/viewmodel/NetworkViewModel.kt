package com.example.nickelfoxassignment.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.nickelfoxassignment.newsapp.connectivity.ListenNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(listenNetwork: ListenNetwork) : ViewModel() {

    val isConnected: LiveData<Boolean> = listenNetwork.isConnected.asLiveData()

}