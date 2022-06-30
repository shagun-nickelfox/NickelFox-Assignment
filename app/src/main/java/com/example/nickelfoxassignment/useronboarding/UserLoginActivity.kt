package com.example.nickelfoxassignment.useronboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.databinding.ActivityUserLoginBinding
import com.google.firebase.auth.FirebaseAuth

class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@UserLoginActivity,
                Pair.create(binding.ivLogo, "logoImage"),
                Pair.create(binding.tvTitle, "logoText"),
                Pair.create(binding.tvSubtitle, "description"),
                Pair.create(binding.tvEmail, "username"),
                Pair.create(binding.tvPassword, "password"),
                Pair.create(binding.btnGo, "login_btn"),
                Pair.create(binding.btnSignup, "signup_btn")
            )
            startActivity(intent, options.toBundle())
            finish()
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnGo.setOnClickListener {
            val userName = binding.tvEmailInput.text.toString()
            val passWord = binding.tvPasswordInput.text.toString()
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("Name", userName)
            editor.commit()
            editor.apply()

            if (userName.isNotEmpty() && passWord.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(userName, passWord)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this@UserLoginActivity,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(this@UserLoginActivity, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@UserLoginActivity,
                                it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this@UserLoginActivity,
                    "Empty fields are not allowed!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this@UserLoginActivity, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}