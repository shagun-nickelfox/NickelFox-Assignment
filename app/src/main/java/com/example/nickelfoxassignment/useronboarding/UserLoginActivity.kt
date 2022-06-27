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

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        binding.signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@UserLoginActivity,
                Pair.create(binding.logoImage, "logoImage"),
                Pair.create(binding.logoText, "logoText"),
                Pair.create(binding.text, "description"),
                Pair.create(binding.textemail, "username"),
                Pair.create(binding.textpassword, "password"),
                Pair.create(binding.go, "login_btn"),
                Pair.create(binding.signup, "signup_btn")
            )
            startActivity(intent, options.toBundle())
            finish()
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.go.setOnClickListener {
            val userName = binding.email.text.toString()
            val passWord = binding.password.text.toString()
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