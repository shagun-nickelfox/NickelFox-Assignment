package com.example.nickelfoxassignment.crashlytics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nickelfoxassignment.databinding.ActivityCrashBinding
import kotlinx.android.synthetic.main.activity_crash.*
import java.lang.RuntimeException

class CrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnCrash.setOnClickListener {
            throw RuntimeException("Crashed")
        }
    }
}