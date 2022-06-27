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

        binding.apply {
            image.animation = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.top_animation)
            text.animation =
                AnimationUtils.loadAnimation(this@LoginActivity, R.anim.bottom_animation)

        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, UserLoginActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@LoginActivity,
                Pair.create(binding.image, "logoImage"), Pair.create(binding.text, "logoText")
            )
            startActivity(intent, options.toBundle())
            finish()
        }, 3000)
    }
}