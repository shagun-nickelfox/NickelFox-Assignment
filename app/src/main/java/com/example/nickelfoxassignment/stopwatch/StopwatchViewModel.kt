package com.example.nickelfoxassignment.stopwatch

import android.content.Context
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.nickelfoxassignment.Constants

class StopwatchViewModel : ViewModel() {
    private var running = MutableLiveData(false)
    private lateinit var owner: LifecycleOwner
    private lateinit var context: Context

    fun attach(lifecycleOwner: LifecycleOwner, c: Context) {
        owner = lifecycleOwner
        context = c
    }

    fun setRunningValue(runningValue: Boolean) {
        running.value = runningValue
    }
    val getTopHeadlines = running.switchMap {
        val data =
            Data.Builder().putBoolean("running", running.value!!)
                .build()
        val myWorkRequest =
            OneTimeWorkRequest.Builder(TimerWorker::class.java).setInputData(data).build()
        val workManager = WorkManager.getInstance(context)
        workManager.beginWith(myWorkRequest).enqueue()
        return@switchMap Constants.DATA
    }
}