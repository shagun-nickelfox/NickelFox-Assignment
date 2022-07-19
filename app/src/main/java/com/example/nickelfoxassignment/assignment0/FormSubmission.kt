package com.example.nickelfoxassignment.assignment0

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RatingBar
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityFormSubmissionBinding
import com.example.nickelfoxassignment.showToolbar
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
        finish()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
        super.onBackPressed()
    }

    private fun setupToolbar() {
        setSupportActionBar(
            binding.toolbar.root.showToolbar(
                "Form Submission",
                R.color.purple_700,
                R.color.yellow
            )
        )
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
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        hideKeyboard(v)
                        datePickerDialog.show()
                    }
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
                val name = tvNameInput.text.toString()
                val dob = tvDOBInput.text.toString()
                message =
                    "\n Name: $name \n Date Of Birth: $dob \n Gender: $gender \n Language: $language \n Rate Us: $rate"

                if (checkBox.isChecked && name.isNotEmpty() && dob.isNotEmpty()
                    && gender.toString().isNotEmpty() && language.toString()
                        .isNotEmpty() && rate.toString().isNotEmpty()
                ) {
                    showDialog()
                } else {
                    Snackbar.make(
                        it,
                        "Empty fields are not allowed!",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    /**
     * Show a dialog to confirm whether user wants to share the data or not
     */

    private fun showDialog() {
        MaterialAlertDialogBuilder(this@FormSubmission)
            .setTitle("Are you sure you want to share?")
            .setMessage(message)
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
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}