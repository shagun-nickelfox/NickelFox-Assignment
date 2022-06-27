package com.example.nickelfoxassignment.crashlytics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.nickelfoxassignment.R
import java.lang.RuntimeException

class CrashActivity : AppCompatActivity() {
    lateinit var  button : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)

        button = findViewById(R.id.button)

        button.setOnClickListener{
            throw RuntimeException("Crashed")
        }
    }
}