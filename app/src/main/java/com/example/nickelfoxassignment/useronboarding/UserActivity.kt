package com.example.nickelfoxassignment.useronboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nickelfoxassignment.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val name = preferences.getString("Name", "")
        binding.text2.text = name

        binding.logout.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this@UserActivity, UserLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}