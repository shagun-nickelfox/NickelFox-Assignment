package com.example.nickelfoxassignment.stopwatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityStopwatchBinding


class StopwatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStopwatchBinding
    private lateinit var lapAdapter: LapAdapter

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

    override fun onPause() {
        saveData()
        super.onPause()
    }

    private fun setupListeners() {
        binding.apply {
            tvReset.setOnClickListener {
                ivPlay.isVisible = true
                ivPause.isVisible = false
                lapAdapter.clearList()
                startAndStopWorker(false)
                Constants.DATA.value = getString(R.string.time_stopwatch)
                Constants.SECONDS.value = 0
                tvTime.text = Constants.DATA.value
            }
            ivPlayCircle.setOnClickListener {
                if (ivPlay.isVisible) {
                    ivPause.isVisible = true
                    ivPlay.isVisible = false
                    startAndStopWorker(true)
                } else {
                    ivPause.isVisible = false
                    ivPlay.isVisible = true
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
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        val set: MutableSet<String> = HashSet()
        set.addAll(lapAdapter.list)
        myEdit.putBoolean("play", binding.ivPlay.isVisible)
        myEdit.putBoolean("pause", binding.ivPause.isVisible)
        myEdit.putStringSet("lapList", set)
        myEdit.apply()
    }

    private fun startAndStopWorker(running: Boolean) {
        val data =
            Data.Builder().putBoolean("running", running)
                .build()
        val myWorkRequest =
            OneTimeWorkRequest.Builder(TimerWorker::class.java).setInputData(data).build()
        WorkManager.getInstance(applicationContext).beginWith(myWorkRequest).enqueue()
    }

    private fun setupObservers() {
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        binding.ivPlay.isVisible = sh.getBoolean("play", true)
        binding.ivPause.isVisible = sh.getBoolean("pause", false)
        val list = sh.getStringSet("lapList", null)
        if (list != null) {
            for (i in list) {
                lapAdapter.addItemToList(i)
            }
        }
        Constants.DATA.observe(this@StopwatchActivity) { data ->
            binding.tvTime.text = data
        }
    }
}

