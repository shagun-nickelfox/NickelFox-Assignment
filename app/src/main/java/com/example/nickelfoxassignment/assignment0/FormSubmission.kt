package com.example.nickelfoxassignment.assignment0

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import androidx.core.content.ContextCompat
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityFormSubmissionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.*

class FormSubmission : AppCompatActivity() {
    private lateinit var message: String
    private lateinit var datePickerDialog: DatePickerDialog
    private var gender: String? = null
    private var language: String? = null
    private var rate: Float = 0.0f
    private val c = Calendar.getInstance()
    private val mYear = c.get(Calendar.YEAR)
    private val mMonth = c.get(Calendar.MONTH)
    private val mDay = c.get(Calendar.DAY_OF_MONTH)

    private lateinit var binding: ActivityFormSubmissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormSubmissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setUpDatePicker()
        setUpListeners()

    }

    override fun onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
        super.onBackPressed()
    }

    private fun setupToolbar() {
        binding.toolbar.root.apply {
            title = "Form Submission"
            setBackgroundColor(ContextCompat.getColor(this@FormSubmission, R.color.yellow))
            setTitleTextColor(ContextCompat.getColor(this@FormSubmission, R.color.purple_700))
            setSupportActionBar(this)
        }
    }



    private fun setUpDatePicker() {
        datePickerDialog = DatePickerDialog(
            this@FormSubmission,
            { _, year, month, dayOfMonth ->
                binding.tvDOBInput.setText("$dayOfMonth/${(month + 1)}/$year")
            },
            mYear,
            mMonth,
            mDay
        )
    }

    private fun setUpListeners() {
        binding.apply {
            tvDOBInput.apply {
                setOnClickListener {
                    datePickerDialog.show()
                }
            }
            rgGenderOptions.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbMale -> {
                        gender = "Male"
                    }
                    R.id.rbFemale -> {
                        gender = "Female"
                    }
                }
            }
            switchHindi.setOnCheckedChangeListener { _, onSwitch ->
                if (onSwitch) {
                    language = "Hindi"
                }
            }
            switchEnglish.setOnCheckedChangeListener { _, onSwitch ->
                if (onSwitch) {
                    language = "English"
                }
            }

           rbBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    rate = rating
                }

            btnSubmit.setOnClickListener {
                message =
                    "Name: ${tvNameInput.text.toString()} \n Date Of Birth: ${tvDOBInput.text.toString()} \n Gender: $gender \n Language: $language \n Rate Us: $rate"

                if (checkBox.isChecked) {
                    MaterialAlertDialogBuilder(this@FormSubmission)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to share?")
                        .setNegativeButton("No") { _, _ ->
                        }
                        .setPositiveButton("Yes") { _, _ ->

                            val intent = Intent()
                            intent.action = Intent.ACTION_SEND
                            intent.putExtra(Intent.EXTRA_TEXT, message)
                            intent.setPackage("com.whatsapp")
                            intent.type = "text/plain"

                            startActivity(Intent.createChooser(intent, "Share to : "))
                        }
                        .show()
                } else {
                    Snackbar.make(
                        it,
                        "Please accept the terms and conditions",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}