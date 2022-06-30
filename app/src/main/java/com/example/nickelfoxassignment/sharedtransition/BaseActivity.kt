package com.example.nickelfoxassignment.sharedtransition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.root.apply {
            title = "Base Activity"
            setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.rectangle_2))
            setTitleTextColor(ContextCompat.getColor(this@BaseActivity, android.R.color.white))
            setSupportActionBar(this)
        }
    }

    private fun setupListeners() {
        binding.apply {
            card.setOnClickListener {
                val intent = Intent(this@BaseActivity, DetailedActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@BaseActivity,
                    Pair.create(profile, "profile"), Pair.create(title, "title")
                )
                startActivity(intent, options.toBundle())
            }
        }
    }
}