package com.example.nickelfoxassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.nickelfoxassignment.assignment0.ButtonsActivity
import com.example.nickelfoxassignment.crashlytics.CrashActivity
import com.example.nickelfoxassignment.databinding.ActivityMainBinding
import com.example.nickelfoxassignment.demomap.MapsActivity
import com.example.nickelfoxassignment.sharedtransition.BaseActivity
import com.example.nickelfoxassignment.useronboarding.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "NickelFox Assignment"
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
        setSupportActionBar(toolbar)
    }

    fun assignment(view: View) {
        val intent = Intent(this, ButtonsActivity::class.java)
        startActivity(intent)
        println("*")
    }

    fun clustering(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun transition(view: View) {
        val intent = Intent(this, BaseActivity::class.java)
        startActivity(intent)
    }

    fun onboarding(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun crashlytics(view: View) {
        val intent = Intent(this, CrashActivity::class.java)
        startActivity(intent)
    }

}