package com.example.nickelfoxassignment.usersonboarding

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityUserLoginBinding
import com.example.nickelfoxassignment.databinding.DialogForgotPasswordBinding
import com.example.nickelfoxassignment.longToast
import com.example.nickelfoxassignment.shortToast
import com.example.nickelfoxassignment.showAnotherActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_login.*


class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding
    private lateinit var dialog: Dialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpForgotPasswordDialog()
        setupListeners()
    }

    private fun saveDataToSharedPreferences(username: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("Name", username)
        editor.commit()
        editor.apply()
    }

    /**
     * Create animation when user switch to new activity
     */
    private fun createAnimations() {
        val intent = Intent(this@UserLoginActivity, SignUpActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@UserLoginActivity,
            Pair.create(ivLogo, "logoImage"),
            Pair.create(tvTitle, "logoText"),
            Pair.create(tvSubtitle, "description"),
            Pair.create(tvEmail, "username"),
            Pair.create(tvPassword, "password"),
            Pair.create(btnGo, "login_btn"),
            Pair.create(btnSignup, "signup_btn")
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    /**
     * Initialize forgot password dialog
     */
    private fun setUpForgotPasswordDialog() {
        dialog = Dialog(this)
        val dialogBinding = DialogForgotPasswordBinding.inflate(layoutInflater)
        dialogBinding.apply {
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnReset.setOnClickListener {
                forgotPassword(dialogBinding.editText)
            }
        }
        dialog.setContentView(dialogBinding.root)
    }

    private fun setupListeners() {
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        binding.apply {
            btnSignup.setOnClickListener {
                createAnimations()
            }

            btnForgetPassword.setOnClickListener {
                dialog.show()
            }

            firebaseAuth = FirebaseAuth.getInstance()

            btnGo.setOnClickListener {
                showProgressBar()
                val userName = tvEmailInput.text.toString()
                val passWord = tvPasswordInput.text.toString()

                saveDataToSharedPreferences(userName)

                if (userName.isNotEmpty() && passWord.isNotEmpty()) {
                    signIn(userName, passWord)
                } else {
                    hideProgressBar()
                    this@UserLoginActivity.shortToast("Empty fields are not allowed!!")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            this@UserLoginActivity.showAnotherActivity(UserActivity::class.java)
            finish()
        }
    }

    private fun forgotPassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            this.shortToast("Please enter email address")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            this.shortToast("Invalid email address")
            return
        }

        showProgressBar()

        sendPasswordResetEmail(username.text.toString())
    }

    /**
     * SignIn with the entered email and password bby user
     */

    private fun signIn(username: String, passWord: String) {
        firebaseAuth.signInWithEmailAndPassword(username, passWord)
            .addOnCompleteListener {
                hideProgressBar()
                if (it.isSuccessful) {
                    this@UserLoginActivity.shortToast("Login Successful")
                    this@UserLoginActivity.showAnotherActivity(UserActivity::class.java)
                    finish()
                } else {
                    this@UserLoginActivity.shortToast(it.exception?.message!!)
                }
            }
    }

    /**
     * send a email that contains password reset link
     */

    private fun sendPasswordResetEmail(username: String) {
        firebaseAuth.sendPasswordResetEmail(username)
            .addOnCompleteListener { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    this.longToast("Password reset link sent to your email")
                    dialog.dismiss()
                } else
                    this.shortToast(task.exception?.message!!)
            }.addOnFailureListener {
                hideProgressBar()
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