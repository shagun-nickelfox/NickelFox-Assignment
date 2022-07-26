package com.example.nickelfoxassignment.assignment0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityCalculatorUiBinding
import com.example.nickelfoxassignment.showToolbar
import kotlinx.android.synthetic.main.activity_calculator_ui.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class CalculatorUI : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupValues()
    }

    private fun setupToolbar() {
        setSupportActionBar(
            binding.toolbar.root.showToolbar(
                "Calculator",
                android.R.color.white,
                R.color.rectangle_2
            )
        )
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        super.onBackPressed()
    }

    private fun setupValues() {

        /*Number Buttons*/
        binding.apply {
            tvOne.setOnClickListener {
                evaluateExpression("1")
            }

            tvTwo.setOnClickListener {
                evaluateExpression("2")
            }

            tvThree.setOnClickListener {
                evaluateExpression("3")
            }
            tvFour.setOnClickListener {
                evaluateExpression("4")
            }

            tvFive.setOnClickListener {
                evaluateExpression("5")
            }

            tvSix.setOnClickListener {
                evaluateExpression("6")
            }

            tvSeven.setOnClickListener {
                evaluateExpression("7")
            }

            tvEight.setOnClickListener {
                evaluateExpression("8")
            }

            tvNine.setOnClickListener {
                evaluateExpression("9")
            }

            tvZero.setOnClickListener {
                evaluateExpression("0")
            }

            tvDoubleZero.setOnClickListener {
                evaluateExpression("00")
            }

            /* Trigonometric Functions */

            tvSin.setOnClickListener {
                evaluateExpression("sin ")
            }

            tvCos.setOnClickListener {
                evaluateExpression("cos ")
            }

            tvTan.setOnClickListener {
                evaluateExpression("tan ")
            }

            /*Operators*/

            tvPlus.setOnClickListener {
                evaluateExpression("+")
            }

            tvSubtract.setOnClickListener {
                evaluateExpression("-")
            }

            tvMultiply.setOnClickListener {
                evaluateExpression("*")
            }

            tvDivide.setOnClickListener {
                evaluateExpression("/")
            }

            tvDot.setOnClickListener {
                evaluateExpression(".")
            }

            tvPercent.setOnClickListener {
                evaluateExpression("%")
            }

            tvPlusMinus.setOnClickListener {
                textView1.text = (-1 * textView1.text.toString().toDouble()).toString()
            }

            tvAC.setOnClickListener {
                textView1.text = ""
                tvResult.text = ""
            }

            tvEqual.setOnClickListener {
                val expression = ExpressionBuilder(getInputExpression()).build()
                try {
                    val result = expression.evaluate()
                    tvResult.text = DecimalFormat("0.######").format(result).toString()
                } catch (e: Exception) {
                    tvResult.text = "Error"
                    Toast.makeText(this@CalculatorUI, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

/*Function to evaluate the expressions entered by user using NumPad*/

    private fun evaluateExpression(string: String) {
        tvResult.text = ""
        textView1.append(string)
    }

    private fun getInputExpression(): String {
        var expression = textView1.text.replace(Regex("÷"), "/")
        expression = expression.replace(Regex("×"), "*")
        return expression
    }
}