package com.example.nickelfoxassignment.assignment0

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityButtonsBinding

class ButtonsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityButtonsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityButtonsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.root.apply {
            title = "First Assignment"
            setBackgroundColor(ContextCompat.getColor(this@ButtonsActivity, R.color.skyBlue))
            setTitleTextColor(ContextCompat.getColor(this@ButtonsActivity, android.R.color.white))
            setSupportActionBar(this)
        }
    }

    private fun setupListeners() {
        binding.apply {
            cvFormSubmission.setOnClickListener {
                val intent = Intent(this@ButtonsActivity, FormSubmission::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_in_right
                )
            }

            cvAnimation.setOnClickListener {
                val intent = Intent(this@ButtonsActivity, AnimationsDemoScreen::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }

            cvCalculator.setOnClickListener {
                val intent = Intent(this@ButtonsActivity, CalculatorUI::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.zoom_in, 0)
            }

            cvGoogleMaps.setOnClickListener {
                val intent = Intent(this@ButtonsActivity, GoogleMapsScreen::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out)
            }
        }
    }
}