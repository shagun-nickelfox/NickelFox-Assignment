package com.example.nickelfoxassignment.assignment0

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityButtonsBinding
import com.example.nickelfoxassignment.showAnotherActivity
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
                this@ButtonsActivity.showAnotherActivity(FormSubmission::class.java)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right)
            }

            cvAnimation.setOnClickListener {
                this@ButtonsActivity.showAnotherActivity(AnimationsDemoScreen::class.java)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }

            cvCalculator.setOnClickListener {
                this@ButtonsActivity.showAnotherActivity(CalculatorUI::class.java)
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            }

            cvGoogleMaps.setOnClickListener {
                this@ButtonsActivity.showAnotherActivity(GoogleMapsScreen::class.java)
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out)
            }
        }
    }
}