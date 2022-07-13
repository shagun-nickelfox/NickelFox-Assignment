package com.example.nickelfoxassignment.userOnBoarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.databinding.ActivityUserLoginBinding
import com.example.nickelfoxassignment.databinding.DialogForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_login.*


class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding
    private lateinit var forgotPasswordBinding: DialogForgotPasswordBinding
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

        btnForgetPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            forgotPasswordBinding = DialogForgotPasswordBinding.inflate(layoutInflater)
            builder.setView(forgotPasswordBinding.root)
            builder.setPositiveButton("Reset") { _, _ ->
                forgotPassword(forgotPasswordBinding.editText)
            }
            builder.setNegativeButton("Close") { _, _ -> }
            builder.show()
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

    private fun forgotPassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }

        firebaseAuth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error Failed", Toast.LENGTH_LONG).show()
            }
    }
}