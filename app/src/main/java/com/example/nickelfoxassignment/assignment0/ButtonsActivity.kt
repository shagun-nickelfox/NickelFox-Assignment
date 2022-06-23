package com.example.nickelfoxassignment.assignment0

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.nickelfoxassignment.R


class ButtonsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttons)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Assignment"
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
        setSupportActionBar(toolbar)
    }

    fun formSubmission(view: View) {
        val intent = Intent(this, FormSubmission::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_in_left
        )
    }

    fun animationsDemoScreen(view: View) {
        val intent = Intent(this, AnimationsDemoScreen::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun calculatorUI(view: View) {
        val intent = Intent(this, CalculatorUI::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.rotate_clock, R.anim.rotate_anti)
    }

    fun googleMapsScreen(view: View) {
        val intent = Intent(this, GoogleMapsScreen::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.move, R.anim.move)
    }
}