package com.example.nickelfoxassignment.assignment0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityCalculatorUiBinding

class CalculatorUI : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    override fun onBackPressed() {
        finish();
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out)
        super.onBackPressed()
    }

    private fun setupToolbar() {
        binding.toolbar?.root?.apply {
            title = "Calculator UI"
            setBackgroundColor(ContextCompat.getColor(this@CalculatorUI, R.color.rectangle_2))
            setTitleTextColor(ContextCompat.getColor(this@CalculatorUI, android.R.color.white))
            setSupportActionBar(this)
        }
    }
}