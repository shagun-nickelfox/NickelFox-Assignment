package com.example.nickelfoxassignment.assignment0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityCalculatorUiBinding
import com.example.nickelfoxassignment.showToolbar

class CalculatorUI : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(
            binding.toolbar?.root?.showToolbar(
                "Calculator UI",
                android.R.color.white,
                R.color.rectangle_2
            )
        )
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        super.onBackPressed()
    }
}