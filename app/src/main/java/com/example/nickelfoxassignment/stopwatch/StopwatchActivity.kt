package com.example.nickelfoxassignment.stopwatch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityStopwatchBinding

class StopwatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStopwatchBinding
    private val viewModel by viewModels<StopwatchViewModel>()
    private lateinit var lapAdapter: LapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.attach(this, this)
        setupAdapter()
        setupListeners()
    }

    private fun setupAdapter() {
        lapAdapter = LapAdapter(this)
        binding.rvLapTime.adapter = lapAdapter
        binding.rvLapTime.layoutManager = LinearLayoutManager(this)
    }

    private fun setupListeners() {
        binding.apply {
            tvReset.setOnClickListener {
                ivPlay.isVisible = true
                ivPause.isVisible = false
                Constants.DATA.value = null
                viewModel.setRunningValue(false)
                Constants.SECONDS.value = 0
                tvTime.text = getString(R.string.time_stopwatch)
            }
            ivPlayCircle.setOnClickListener {
                if (ivPlay.isVisible) {
                    ivPause.isVisible = true
                    ivPlay.isVisible = false
                    viewModel.setRunningValue(true)
                    viewModel.getTopHeadlines.observe(this@StopwatchActivity) { data ->
                        tvTime.text = data
                    }
                } else {
                    ivPause.isVisible = false
                    ivPlay.isVisible = true
                    viewModel.setRunningValue(false)
                }
            }
            tvLap.setOnClickListener {
                lapAdapter.addItemToList(tvTime.text.toString())
            }
        }
    }
}

