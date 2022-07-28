package com.example.nickelfoxassignment.assignment0

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityCalculatorUiBinding
import com.example.nickelfoxassignment.showToolbar
import kotlinx.android.synthetic.main.activity_calculator_ui.*
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorUI : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorUiBinding

    companion object {
        //private const val ERROR_MESSAGE = "Error"
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
                resultText()
            }

            tvTwo.setOnClickListener {
                evaluateExpression(Constants.TWO)
                resultText()
            }

            tvThree.setOnClickListener {
                evaluateExpression(Constants.THREE)
                resultText()
            }
            tvFour.setOnClickListener {
                evaluateExpression(Constants.FOUR)
                resultText()
            }

            tvFive.setOnClickListener {
                evaluateExpression(Constants.FIVE)
                resultText()
            }

            tvSix.setOnClickListener {
                evaluateExpression(Constants.SIX)
                resultText()
            }

            tvSeven.setOnClickListener {
                evaluateExpression(Constants.SEVEN)
                resultText()
            }

            tvEight.setOnClickListener {
                evaluateExpression(Constants.EIGHT)
                resultText()
            }

            tvNine.setOnClickListener {
                evaluateExpression(Constants.NINE)
                resultText()
            }

            tvZero.setOnClickListener {
                evaluateExpression(Constants.ZERO)
                resultText()
            }

            tvDoubleZero.setOnClickListener {
                evaluateExpression(Constants.DOUBLE_ZERO)
                resultText()
            }
            /*Operators*/

            tvPlus.setOnClickListener {
                evaluateExpression(Constants.ADD)
                resultText()
            }

            tvSubtract.setOnClickListener {
                evaluateExpression(Constants.SUBTRACT)
                resultText()
            }

            tvMultiply.setOnClickListener {
                evaluateExpression(Constants.MULTIPLY)
                resultText()
            }

            tvDivide.setOnClickListener {
                evaluateExpression(Constants.DIVIDE)
                resultText()
            }

            tvDot.setOnClickListener {
                evaluateExpression(Constants.DOT)
            }

            tvMod.setOnClickListener {
                evaluateExpression(Constants.MOD)
                resultText()
            }

            tvAC.setOnClickListener {
                textView1.text = EMPTY_STRING
                tvResult.text = EMPTY_STRING
            }

            tvBack.setOnClickListener {
                if (textView1.text.toString().isNotEmpty() || textView1.text.toString() != Constants.DOT) {
                    val lastIndex = textView1.text.toString().lastIndex
                    textView1.text = textView1.text.toString().substring(0, lastIndex)
                    resultText()
                } else {
                    textView1.text = EMPTY_STRING
                    tvResult.text = EMPTY_STRING
                }
            }

            tvEqual.setOnClickListener {
                tvResult.textSize = 50F
                resultText()
            }
        }
    }

/*Function to evaluate the expressions entered by user using NumPad*/

    private fun evaluateExpression(string: String) {
        tvResult.text = EMPTY_STRING
        textView1.append(string)
    }

    /* function to calculate result of the expression entered by user */

    private fun resultText() {
        if (getInputExpression().isNotEmpty()) {
            val expression = ExpressionBuilder(getInputExpression()).build()
            try {
                val result = expression.evaluate()
                tvResult.text = result.toString()
            } catch (e: Exception) {
                textView1.text = textView1.text.toString()
                tvResult.text = EMPTY_STRING
                //Toast.makeText(this@CalculatorUI, e.message, Toast.LENGTH_LONG).show()
            }
        } else {
            tvResult.text = EMPTY_STRING
        }
    }

    private fun getInputExpression(): String {
        var expression = textView1.text.replace(Regex(Constants.DIVIDE_), Constants.DIVIDE)
        expression = expression.replace(Regex(Constants.MULTIPLY_), Constants.MULTIPLY)
        return expression
    }
}