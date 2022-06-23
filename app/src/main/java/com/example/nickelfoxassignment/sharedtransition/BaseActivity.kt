package com.example.nickelfoxassignment.sharedtransition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

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