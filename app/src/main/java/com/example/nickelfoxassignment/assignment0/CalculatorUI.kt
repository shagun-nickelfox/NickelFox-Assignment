package com.example.nickelfoxassignment.assignment0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityCalculatorUiBinding
import com.example.nickelfoxassignment.showToolbar
import kotlinx.android.synthetic.main.activity_calculator_ui.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class CalculatorUI : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorUiBinding

    companion object {
        private const val ERROR_MESSAGE = "Error"
        private const val EMPTY_STRING = ""
    }

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
                evaluateExpression(Constants.ONE)
            }

            tvTwo.setOnClickListener {
                evaluateExpression(Constants.TWO)
            }

            tvThree.setOnClickListener {
                evaluateExpression(Constants.THREE)
            }
            tvFour.setOnClickListener {
                evaluateExpression(Constants.FOUR)
            }

            tvFive.setOnClickListener {
                evaluateExpression(Constants.FIVE)
            }

            tvSix.setOnClickListener {
                evaluateExpression(Constants.SIX)
            }

            tvSeven.setOnClickListener {
                evaluateExpression(Constants.SEVEN)
            }

            tvEight.setOnClickListener {
                evaluateExpression(Constants.EIGHT)
            }

            tvNine.setOnClickListener {
                evaluateExpression(Constants.NINE)
            }

            tvZero.setOnClickListener {
                evaluateExpression(Constants.ZERO)
            }

            tvDoubleZero.setOnClickListener {
                evaluateExpression(Constants.DOUBLE_ZERO)
            }

            /* Trigonometric Functions */

            tvSin.setOnClickListener {
                evaluateExpression(Constants.SIN)
            }

            tvCos.setOnClickListener {
                evaluateExpression(Constants.COS)
            }

            tvTan.setOnClickListener {
                evaluateExpression(Constants.TAN)
            }

            /*Operators*/

            tvPlus.setOnClickListener {
                evaluateExpression(Constants.ADD)
            }

            tvSubtract.setOnClickListener {
                evaluateExpression(Constants.SUBTRACT)
            }

            tvMultiply.setOnClickListener {
                evaluateExpression(Constants.MULTIPLY)
            }

            tvDivide.setOnClickListener {
                evaluateExpression(Constants.DIVIDE)
            }

            tvDot.setOnClickListener {
                evaluateExpression(Constants.DOT)
            }

            tvMod.setOnClickListener {
                evaluateExpression(Constants.MOD)
            }

            tvPlusMinus.setOnClickListener {
                textView1.text = (-1 * textView1.text.toString().toDouble()).toString()
            }

            tvAC.setOnClickListener {
                textView1.text = EMPTY_STRING
                tvResult.text = EMPTY_STRING
            }

            tvEqual.setOnClickListener {
                val expression = ExpressionBuilder(getInputExpression()).build()
                try {
                    val result = expression.evaluate()
                    tvResult.text =
                        DecimalFormat(Constants.DECIMAL_FORMAT).format(result).toString()
                } catch (e: Exception) {
                    tvResult.text = ERROR_MESSAGE
                    Toast.makeText(this@CalculatorUI, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

/*Function to evaluate the expressions entered by user using NumPad*/

    private fun evaluateExpression(string: String) {
        tvResult.text = EMPTY_STRING
        textView1.append(string)
    }

    private fun getInputExpression(): String {
        var expression = textView1.text.replace(Regex(Constants.DIVIDE_), Constants.DIVIDE)
        expression = expression.replace(Regex(Constants.MULTIPLY_), Constants.MULTIPLY)
        return expression
    }
}