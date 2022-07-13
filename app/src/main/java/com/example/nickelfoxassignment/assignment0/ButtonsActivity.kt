package com.example.nickelfoxassignment.assignment0

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityButtonsBinding
import com.example.nickelfoxassignment.showToolbar

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
        setSupportActionBar(
            binding.toolbar.root.showToolbar(
                "First Assignment",
                android.R.color.white,
                R.color.skyBlue
            )
        )
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
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            }

            cvGoogleMaps.setOnClickListener {
                val intent = Intent(this@ButtonsActivity, GoogleMapsScreen::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out)
            }
        }
    }
}