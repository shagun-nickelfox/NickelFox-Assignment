package com.example.nickelfoxassignment.assignment0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityAnimationsDemoScreenBinding

class AnimationsDemoScreen : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationsDemoScreenBinding
    private lateinit var animation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationsDemoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    override fun onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, 0)
        super.onBackPressed()
    }

    private fun setupToolbar() {
        binding.toolbar.root.apply {
            title = "Animation Demo Screen"
            setBackgroundColor(
                ContextCompat.getColor(
                    this@AnimationsDemoScreen,
                    R.color.purple_700
                )
            )
            setTitleTextColor(
                ContextCompat.getColor(
                    this@AnimationsDemoScreen,
                    android.R.color.white
                )
            )
            setSupportActionBar(this)
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnRotation.setOnClickListener {
                animation = AnimationUtils.loadAnimation(this@AnimationsDemoScreen, R.anim.clockwise)
                ivBartImage.startAnimation(animation)
            }

            btnBlink.setOnClickListener {
                animation = AnimationUtils.loadAnimation(this@AnimationsDemoScreen, R.anim.blink)
                ivBartImage.startAnimation(animation)
            }

            btnFade.setOnClickListener {
                animation = AnimationUtils.loadAnimation(this@AnimationsDemoScreen, R.anim.fade)
                ivBartImage.startAnimation(animation)
            }

            btnMove.setOnClickListener {
                animation = AnimationUtils.loadAnimation(this@AnimationsDemoScreen, R.anim.move)
                ivBartImage.startAnimation(animation)
            }

            btnZoom.setOnClickListener {
                animation = AnimationUtils.loadAnimation(this@AnimationsDemoScreen, R.anim.zoom)
                ivBartImage.startAnimation(animation)
            }

            btnSlide.setOnClickListener {
                animation = AnimationUtils.loadAnimation(this@AnimationsDemoScreen, R.anim.slide)
                ivBartImage.startAnimation(animation)
            }
        }
    }
}