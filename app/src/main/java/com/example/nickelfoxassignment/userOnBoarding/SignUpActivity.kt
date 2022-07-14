package com.example.nickelfoxassignment.userOnBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_login.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnAlreadyAccount.setOnClickListener {
            val intent = Intent(this@SignUpActivity, UserLoginActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(binding.ivLogo, "logoImage"),
                Pair.create(binding.tvTitle, "logoText"),
                Pair.create(binding.tvSubtitle, "description"),
                Pair.create(binding.tvUserName, "username"),
                Pair.create(binding.tvPassword, "password"),
                Pair.create(binding.btnSignup, "login_btn"),
                Pair.create(binding.btnAlreadyAccount, "signup_btn")
            )
            startActivity(intent, options.toBundle())
            finish()
        }
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            showProgressBar()
            val usernameF = binding.tvEmail.text.toString()
            val passwordF = binding.tvPassword.text.toString()
            if (usernameF.isNotEmpty() && passwordF.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(usernameF, passwordF)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            hideProgressBar()
                            val intent =
                                Intent(this@SignUpActivity, UserLoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            hideProgressBar()
                            Toast.makeText(
                                this@SignUpActivity,
                                it.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                hideProgressBar()
                Toast.makeText(
                    this@SignUpActivity,
                    "Empty fields are not allowed!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
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