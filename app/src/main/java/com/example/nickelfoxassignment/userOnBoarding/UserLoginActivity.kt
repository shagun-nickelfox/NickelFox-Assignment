package com.example.nickelfoxassignment.userOnBoarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
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
            builder.setTitle("Forgot password?")
            builder.setMessage("Enter your email id")
            forgotPasswordBinding = DialogForgotPasswordBinding.inflate(layoutInflater)
            builder.setView(forgotPasswordBinding.root)
            builder.setPositiveButton("Reset") { _, _ ->
                forgotPassword(forgotPasswordBinding.editText)
            }
            builder.setNegativeButton("Close") { _, _ -> }
            builder.show()
        }

        /*val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.,null)
        builder.setView(dialogView)
        builder.setTitle("Forgot password?")
        builder.setMessage("Enter your email id")
        builder.setPositiveButton(
            "Reset"
        ) { _, _ -> }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        val b = builder.create()
        b.show()*/

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnGo.setOnClickListener {
            showProgressBar()
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
                            hideProgressBar()
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
                            hideProgressBar()
                            Toast.makeText(
                                this@UserLoginActivity,
                                it.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                hideProgressBar()
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
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        showProgressBar()

        firebaseAuth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                hideProgressBar()
                if (task.isSuccessful)
                    Toast.makeText(
                        this,
                        "Password reset link sent to your email",
                        Toast.LENGTH_LONG
                    ).show()
                else
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                hideProgressBar()
                Toast.makeText(this, "Error in sending email", Toast.LENGTH_LONG).show()
            }
    }

    private fun showProgressBar() {
        progressBar.setBackgroundColor(android.R.color.white)
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