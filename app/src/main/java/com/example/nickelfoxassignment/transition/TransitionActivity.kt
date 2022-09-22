package com.example.nickelfoxassignment.transition

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityTransitionBinding
import com.example.nickelfoxassignment.utils.showToolbar

class TransitionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransitionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(
            binding.toolbar.root.showToolbar(
                "Transition Activity",
                android.R.color.white,
                R.color.rectangle_2
            )
        )
    }

    private fun setupListeners() {
        binding.apply {
            btnAnimation.setOnClickListener {
                animation()
            }
        }
    }

    private fun animation() {
        val anim1 = ObjectAnimator.ofFloat(binding.llCard, View.SCALE_X, 2f)
        anim1.duration = 800
        val anim2 = ObjectAnimator.ofFloat(binding.llCard, View.SCALE_Y, 2f)
        anim2.duration = 800
        val anim = ObjectAnimator.ofFloat(binding.llCard, "alpha", 1f, 0.2f, 1f)
        anim.duration = 800
        val set = AnimatorSet()
        set.playTogether(anim, anim1, anim2)
        set.start()
    }
}