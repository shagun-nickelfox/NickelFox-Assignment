package com.example.nickelfoxassignment.useronboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nickelfoxassignment.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alreadyAccount.setOnClickListener {
            val intent = Intent(this@SignUpActivity, UserLoginActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(binding.logoImage, "logoImage"),
                Pair.create(binding.logoText, "logoText"),
                Pair.create(binding.text, "description"),
                Pair.create(binding.username, "username"),
                Pair.create(binding.password, "password"),
                Pair.create(binding.signup, "login_btn"),
                Pair.create(binding.alreadyAccount, "signup_btn")
            )
            startActivity(intent, options.toBundle())
            finish()
        }
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            val usernameF = binding.email.text.toString()
            val passwordF = binding.password.text.toString()
            if (usernameF.isNotEmpty() && passwordF.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(usernameF, passwordF)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent =
                                Intent(this@SignUpActivity, UserLoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this@SignUpActivity,
                    "Empty fields are not allowed!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}