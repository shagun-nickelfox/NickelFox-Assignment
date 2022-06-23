package com.example.nickelfoxassignment.assignment0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.nickelfoxassignment.R

class AnimationsDemoScreen : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var image: ImageView
    private lateinit var animation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animations_demo_screen)

        image = findViewById(R.id.imageView)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Animations Demo Screen"
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
        setSupportActionBar(toolbar)
    }

    fun clockwise(view:View) {
        animation = AnimationUtils.loadAnimation(this, R.anim.clockwise)
        image.startAnimation(animation)
    }

    fun fade(view:View) {
        animation = AnimationUtils.loadAnimation(this, R.anim.fade)
        image.startAnimation(animation)
    }

    fun blink(view:View) {
        animation = AnimationUtils.loadAnimation(this, R.anim.blink)
        image.startAnimation(animation)
    }

    fun zoom(view:View) {
        animation = AnimationUtils.loadAnimation(this, R.anim.zoom)
        image.startAnimation(animation)
    }

    fun move(view:View) {
        animation = AnimationUtils.loadAnimation(this, R.anim.move)
        image.startAnimation(animation)
    }

    fun slide(view:View) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide)
        image.startAnimation(animation)
    }
}