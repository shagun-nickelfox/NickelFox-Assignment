package com.example.nickelfoxassignment.userOnBoarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.databinding.ActivitySignUpBinding
import com.example.nickelfoxassignment.shortToast
import com.example.nickelfoxassignment.showAnotherActivity
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

    /**
     * Create animation when user switch to new activity
     */
    private fun createAnimations() {
            val intent = Intent(this@SignUpActivity, UserLoginActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@SignUpActivity,
                Pair.create(ivLogo, "logoImage"),
                Pair.create(tvTitle, "logoText"),
                Pair.create(tvSubtitle, "description"),
                Pair.create(tvUserName, "username"),
                Pair.create(tvPassword, "password"),
                Pair.create(btnSignup, "login_btn"),
                Pair.create(btnAlreadyAccount, "signup_btn")
            )
            startActivity(intent, options.toBundle())
            finish()
    }

    /**
     * Save data to shared preferences
     */

    private fun saveDataToSharedPreferences(username: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("Name", username)
        editor.commit()
        editor.apply()
    }

    private fun setupListeners() {
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        binding.apply {

            btnAlreadyAccount.setOnClickListener {
                createAnimations()
            }

            firebaseAuth = FirebaseAuth.getInstance()

            btnSignup.setOnClickListener {
                val usernameF = tvEmail.text.toString()
                val passwordF = tvPassword.text.toString()
                val phone = inputPhone.text.toString()

                saveDataToSharedPreferences(usernameF)

                if (usernameF.isEmpty() || passwordF.isEmpty() || phone.isEmpty()) {
                    this@SignUpActivity.shortToast("Empty fields are not allowed!!")
                } else if (!isValidMail(usernameF)) {
                    this@SignUpActivity.shortToast("Invalid Email Address. Please enter correct email address.")
                } else if (!isValidMobile(phone)) {
                    this@SignUpActivity.shortToast("Invalid Phone No. Please enter a valid phone no.")
                } else {
                    showProgressBar()
                    createUser(usernameF, passwordF)
                }
            }
        }
    }

    /**
     * create a user in the database with the credentials entered by user
     */

    private fun createUser(username: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    hideProgressBar()
                    this@SignUpActivity.showAnotherActivity(UserLoginActivity::class.java)
                    finish()
                } else {
                    hideProgressBar()
                    this@SignUpActivity.shortToast(it.exception?.message!!)
                }
            }
    }

    /**
     * Checking the entered email is valid or not
     */

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        return Pattern.compile(emailString).matcher(email).matches()
    }

    /**
     * Checking the entered phone no. is valid or not
     */
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