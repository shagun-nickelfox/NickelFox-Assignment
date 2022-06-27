package com.example.nickelfoxassignment.assignment0

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import androidx.appcompat.widget.Toolbar
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityFormSubmissionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException
import java.util.*

class FormSubmission : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
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

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Form Submission"
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
        setSupportActionBar(toolbar)
        datePickerDialog = DatePickerDialog(
            this@FormSubmission,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                binding.textInputDob.setText("$dayOfMonth/${(month + 1)}/$year")
            },
            mYear,
            mMonth,
            mDay
        )

        binding.apply {
            textInputDob.apply {
                setOnClickListener {
                    datePickerDialog.show()
                }
            }
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radio_button_1 -> {
                        gender = "Male"
                    }
                    R.id.radio_button_2 -> {
                        gender = "Female"
                    }
                }
            }
            switch1.setOnCheckedChangeListener { _, onSwitch ->
                if (onSwitch) {
                    language = "Hindi"
                }
            }
            switch2.setOnCheckedChangeListener { _, onSwitch ->
                if (onSwitch) {
                    language = "English"
                }
            }
            rBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    rate = rating
                }
            submitButton.setOnClickListener {
                message =
                    "Name: ${textInput.text.toString()} \n Date Of Birth: ${textInputDob.text.toString()} \n Gender: $gender \n Language: $language \n Rate Us: $rate"

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