package com.example.nickelfoxassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.nickelfoxassignment.assignment0.ButtonsActivity
import com.example.nickelfoxassignment.crashlytics.CrashActivity
import com.example.nickelfoxassignment.databinding.ActivityMainBinding
import com.example.nickelfoxassignment.demomap.MapsActivity
import com.example.nickelfoxassignment.sharedtransition.BaseActivity
import com.example.nickelfoxassignment.useronboarding.LoginActivity
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
        binding.toolbar.root.apply {
            title = "NickelFox Assignment"
            setTitleTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
            setSupportActionBar(this)
        }
    }

    private fun setupListeners() {
        binding.apply {
            cvFirstAssignment.setOnClickListener {
                val intent = Intent(this@MainActivity, ButtonsActivity::class.java)
                startActivity(intent)
            }

            cvMarkerClustering.setOnClickListener {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
            }

            cvSharedTransition.setOnClickListener {
                val intent = Intent(this@MainActivity, BaseActivity::class.java)
                startActivity(intent)
            }

            cvOnBoarding.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            cvCrashlytics.setOnClickListener {
                val intent = Intent(this@MainActivity, CrashActivity::class.java)
                startActivity(intent)
            }

            cvViewPager.setOnClickListener {
                val intent = Intent(this@MainActivity, ViewPagerActivity::class.java)
                startActivity(intent)
            }
        }
    }
}