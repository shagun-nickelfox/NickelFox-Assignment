package com.example.nickelfoxassignment.assignment0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.nickelfoxassignment.R

class CalculatorUI : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_ui)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Calculator UI"
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
        setSupportActionBar(toolbar)
    }
}