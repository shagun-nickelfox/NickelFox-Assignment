package com.example.nickelfoxassignment.useronboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

    }

    /**
     * create animation when user go to next activity
     */
    private fun createAnimation() {
        val intent = Intent(this, UserLoginActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@LoginActivity,
            Pair.create(binding.ivLogo, "logoImage"), Pair.create(binding.tvTitle, "logoText")
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun setupListeners() {
        binding.apply {
            ivLogo.animation =
                AnimationUtils.loadAnimation(this@LoginActivity, R.anim.top_animation)
            tvTitle.animation =
                AnimationUtils.loadAnimation(this@LoginActivity, R.anim.bottom_animation)

        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            createAnimation()
        }, 3000)
    }
}