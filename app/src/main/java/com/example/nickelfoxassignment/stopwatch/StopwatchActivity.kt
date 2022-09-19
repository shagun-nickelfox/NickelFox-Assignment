package com.example.nickelfoxassignment.stopwatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityStopwatchBinding
import kotlin.collections.HashSet


class StopwatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStopwatchBinding
    private lateinit var lapAdapter: LapAdapter
    private var myWorkRequest: OneTimeWorkRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setupAdapter()
        setupListeners()
        setupObservers()
    }

    private fun setupAdapter() {
        lapAdapter = LapAdapter(this)
        binding.rvLapTime.adapter = lapAdapter
        binding.rvLapTime.layoutManager = LinearLayoutManager(this)
    }

    override fun onStop() {
        saveData()
        super.onStop()
    }

    private fun setupListeners() {
        binding.apply {
            tvReset.setOnClickListener {
                startAndStopWorker(false)
                ivPlay.isVisible = true
                ivPause.isVisible = false
                lapAdapter.clearList()
                Constants.IS_RESET.value = true
            }
            ivPlayCircle.setOnClickListener {
                if (ivPlay.isVisible) {
                    ivPause.isVisible = true
                    ivPlay.isVisible = false
                    Constants.IS_RESET.value = false
                    startAndStopWorker(true)
                } else {
                    ivPause.isVisible = false
                    ivPlay.isVisible = true
                    Constants.IS_RESET.value = false
                    startAndStopWorker(false)
                }
            }
            tvLap.setOnClickListener {
                if (ivPause.isVisible)
                    lapAdapter.addItemToList(tvTime.text.toString())
            }
            ivBackArrow.setOnClickListener {
                super.onBackPressed()
            }
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        val set: MutableSet<String> = HashSet()
        set.addAll(lapAdapter.list)
        myEdit.putBoolean(PLAY, binding.ivPlay.isVisible)
        myEdit.putBoolean(PAUSE, binding.ivPause.isVisible)
        myEdit.putStringSet(LAP_LIST, set)
        myEdit.apply()
    }

    private fun startAndStopWorker(running: Boolean) {
        val data =
            Data.Builder().putBoolean(Constants.RUNNING, running)
                .build()
        myWorkRequest =
            OneTimeWorkRequest.Builder(TimerWorker::class.java).setInputData(data).build()
        WorkManager.getInstance(applicationContext).beginWith(myWorkRequest!!).enqueue()
    }

    private fun setupObservers() {
        val sh = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        binding.ivPlay.isVisible = sh.getBoolean(PLAY, true)
        binding.ivPause.isVisible = sh.getBoolean(PAUSE, false)
        val list = sh.getStringSet(LAP_LIST, null)
        if (list != null) {
            for (i in list) {
                lapAdapter.addItemToList(i)
            }
        }
        Constants.DATA.observe(this@StopwatchActivity) { data ->
            binding.tvTime.text = data
        }
    }

    companion object {
        const val PLAY = "play"
        const val PAUSE = "pause"
        const val LAP_LIST = "lapList"
        const val SHARED_PREF = "MySharedPref"
    }
}

