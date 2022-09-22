package com.example.nickelfoxassignment.usersonboarding

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityUserBinding
import com.example.nickelfoxassignment.utils.showAnotherActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_login.*

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    /**
     * Get saved data from shared preferences and put it in textview
     */
    private fun getDataFromSharedPreferences() {
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val name = preferences.getString("Name", "")
        binding.tvUserEmail.text = name
    }

    private fun setupListeners() {
        firebaseAuth = FirebaseAuth.getInstance()

        getDataFromSharedPreferences()

        binding.btnLogout.setOnClickListener {
            showProgressBar()
            firebaseAuth.signOut()
            hideProgressBar()
            this@UserActivity.showAnotherActivity(UserLoginActivity::class.java)
            finish()
        }
    }

    private fun showProgressBar() {
        progressBar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = View.GONE
    }
}