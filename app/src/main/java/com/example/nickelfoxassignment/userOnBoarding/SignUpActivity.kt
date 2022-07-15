package com.example.nickelfoxassignment.userOnBoarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

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
            val usernameF = tvEmail.text.toString()
            val passwordF = tvPassword.text.toString()
            val phone = inputPhone.text.toString()
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("Name", usernameF)
            editor.commit()
            editor.apply()

            if (usernameF.isEmpty() || passwordF.isEmpty() || phone.isEmpty()) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Empty fields are not allowed!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!isValidMail(usernameF)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Invalid email address. Please enter a valid email address.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!isValidMobile(phone)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Invalid Phone No. Please enter a valid phone no.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showProgressBar()
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
            }
        }
    }

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        return Pattern.compile(emailString).matcher(email).matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length == 10
        }
        return false
    }

    private fun showProgressBar() {
        progressBarSign.setBackgroundColor(android.R.color.white)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        progressBarSign.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBarSign.visibility = View.GONE
    }
}