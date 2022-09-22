package com.example.nickelfoxassignment.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.nickelfoxassignment.utils.Constants
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityStopwatchBinding
import com.example.nickelfoxassignment.utils.NotificationActions
import com.example.nickelfoxassignment.utils.getStopwatchTime

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

        registerReceiver(broadcastReceiver, IntentFilter(Constants.BROADCAST_NAME))
        setupAdapter()
        setupListeners()
        setupObservers()
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            intent.extras?.let { b ->
                val play = b.getBoolean(NotificationActions.PLAY.name, false)
                val pause = b.getBoolean(NotificationActions.PAUSE.name, false)
                val reset = b.getBoolean(NotificationActions.RESET.name, false)
                val lap = b.getBoolean(NotificationActions.LAP.name, false)
                if (play || pause)
                    updateUI(play = play, pause = pause, reset = reset)
                else if (lap)
                    addItemToLap()
                saveDataInSharedPref()
            }
        }
    }

    private fun setupAdapter() {
        lapAdapter = LapAdapter(this)
        binding.rvLapTime.adapter = lapAdapter
        binding.rvLapTime.layoutManager = LinearLayoutManager(this)
    }

    override fun onStop() {
        saveDataInSharedPref()
        super.onStop()
    }

    private fun setupListeners() {
        binding.apply {
            tvReset.setOnClickListener {
                startAndStopWorker(reset = true)
                updateUI(play = true, reset = true)
            }
            ivPlayCircle.setOnClickListener {
                updateUI(play = !ivPlay.isVisible, pause = ivPlay.isVisible)
                startAndStopWorker(play = ivPlay.isVisible)
            }
            tvLap.setOnClickListener {
                addItemToLap()
            }
            ivBackArrow.setOnClickListener {
                super.onBackPressed()
            }
        }
    }

    private fun addItemToLap() {
        Constants.SECONDS.value?.let { secs ->
            if (binding.ivPause.isVisible)
                lapAdapter.addItemToList(secs.getStopwatchTime())
        }
    }

    private fun updateUI(
        play: Boolean = false,
        pause: Boolean = false,
        reset: Boolean = false
    ) {
        binding.ivPause.isVisible = pause
        binding.ivPlay.isVisible = play
        if (reset)
            lapAdapter.clearList()
    }

    private fun saveDataInSharedPref() {
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        val set: MutableSet<String> = HashSet()
        set.addAll(lapAdapter.list)
        myEdit.putBoolean(PLAY, binding.ivPlay.isVisible)
        myEdit.putBoolean(PAUSE, binding.ivPause.isVisible)
        myEdit.putStringSet(LAP_LIST, set)
        myEdit.apply()
    }

    private fun startAndStopWorker(
        play: Boolean = false,
        pause: Boolean = false,
        reset: Boolean = false
    ) {
        val data = Data.Builder()
            .putBoolean(NotificationActions.PLAY.name, play)
            .putBoolean(NotificationActions.PAUSE.name, pause)
            .putBoolean(NotificationActions.RESET.name, reset)
            .build()
        myWorkRequest =
            OneTimeWorkRequest.Builder(TimerWorker::class.java).setInputData(data).build()
        WorkManager.getInstance(applicationContext).beginWith(myWorkRequest!!).enqueue()
    }

    private fun setupObservers() {
        val sh = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
        binding.ivPlay.isVisible = sh.getBoolean(PLAY, true)
        binding.ivPause.isVisible = sh.getBoolean(PAUSE, false)
        val list = sh.getStringSet(LAP_LIST, null)
        if (list != null) {
            for (i in list) {
                lapAdapter.addItemToList(i)
            }
        }
        Constants.SECONDS.observe(this@StopwatchActivity) { data ->
            binding.tvTime.text = data.getStopwatchTime()
        }
    }

    companion object {
        const val PLAY = "play"
        const val PAUSE = "pause"
        const val LAP_LIST = "lapList"
    }
}

