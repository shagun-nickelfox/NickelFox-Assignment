package com.example.nickelfoxassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nickelfoxassignment.assignment0.ButtonsActivity
import com.example.nickelfoxassignment.crashlytics.CrashActivity
import com.example.nickelfoxassignment.databinding.ActivityMainBinding
import com.example.nickelfoxassignment.demomap.MapsActivity
import com.example.nickelfoxassignment.sharedtransition.TransitionActivity
import com.example.nickelfoxassignment.userOnBoarding.LoginActivity
import com.example.nickelfoxassignment.viewpager.ViewPagerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(
            binding.toolbar.root.showToolbar(
                "NickelFox Assignment",
                android.R.color.white,
                R.color.purple_700
            )
        )
    }

    private fun setupListeners() {
        binding.apply {
            cvFirstAssignment.setOnClickListener {
                this@MainActivity.showAnotherActivity(ButtonsActivity::class.java)
            }

            cvMarkerClustering.setOnClickListener {
                this@MainActivity.showAnotherActivity(MapsActivity::class.java)
            }

            cvSharedTransition.setOnClickListener {
                this@MainActivity.showAnotherActivity(TransitionActivity::class.java)
            }

            cvOnBoarding.setOnClickListener {
                this@MainActivity.showAnotherActivity(LoginActivity::class.java)
            }

            cvCrashlytics.setOnClickListener {
                this@MainActivity.showAnotherActivity(CrashActivity::class.java)
            }

            cvViewPager.setOnClickListener {
                this@MainActivity.showAnotherActivity(ViewPagerActivity::class.java)
            }
        }
    }
}