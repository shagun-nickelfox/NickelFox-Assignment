package com.example.nickelfoxassignment.assignment0

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nickelfoxassignment.*
import com.example.nickelfoxassignment.assignment0.calculatorhistory.Calculation
import com.example.nickelfoxassignment.assignment0.calculatorhistory.CalculationViewModel
import com.example.nickelfoxassignment.assignment0.calculatorhistory.HistoryActivity
import com.example.nickelfoxassignment.databinding.ActivityCalculatorUiBinding
import kotlinx.android.synthetic.main.activity_calculator_ui.*

class CalculatorUI : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorUiBinding
    private lateinit var viewModel: CalculationViewModel
    private lateinit var calculationType: String
    private lateinit var operands: Array<String>
    private val re = Regex("[0-9]*")
    private val reDecimal = Regex("[0-9]*.[0-9]*")
    private val divisionDecimal = Regex("-?[0-9]*.0")
    private var isDecimal: Boolean = false
    private var equalClicked: Boolean = false

    companion object {
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

        calculationType = intent.getStringExtra("calculationType").toString()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CalculationViewModel::class.java]

        binding.apply {

            if (calculationType == "Edit") {
                textView1.text = intent.getStringExtra("calculationInput")
                tvResult.text = intent.getStringExtra("calculationResult")
            }

            /*Number Buttons*/

            tvOne.setOnClickListener {
                numberClick(Constants.ONE)
            }

            tvTwo.setOnClickListener {
                numberClick(Constants.TWO)
            }

            tvThree.setOnClickListener {
                numberClick(Constants.THREE)
            }
            tvFour.setOnClickListener {
                numberClick(Constants.FOUR)
            }

            tvFive.setOnClickListener {
                numberClick(Constants.FIVE)
            }

            tvSix.setOnClickListener {
                numberClick(Constants.SIX)
            }

            tvSeven.setOnClickListener {
                numberClick(Constants.SEVEN)
            }

            tvEight.setOnClickListener {
                numberClick(Constants.EIGHT)
            }

            tvNine.setOnClickListener {
                numberClick(Constants.NINE)
            }

            tvZero.setOnClickListener {
                numberClick(Constants.ZERO)
            }

            tvDoubleZero.setOnClickListener {
                numberClick(Constants.DOUBLE_ZERO)
            }

            /*Operators*/

            tvPlus.setOnClickListener {
                operatorClick(Constants.ADD)
            }

            tvSubtract.setOnClickListener {
                operatorClick(Constants.SUBTRACT)
            }

            tvMultiply.setOnClickListener {
                operatorClick(Constants.MULTIPLY)
            }

            tvDivide.setOnClickListener {
                operatorClick(Constants.DIVIDE)
            }

            tvMod.setOnClickListener {
                operatorClick(Constants.MOD)
            }

            tvDot.setOnClickListener {
                numberClick(Constants.DOT)
            }

            tvAC.setOnClickListener {
                textView1.text = EMPTY_STRING
                tvResult.text = EMPTY_STRING
                equalClicked = false
            }

            tvBack.setOnClickListener {
                equalClicked = false
                if (textView1.text.toString()
                        .isNotEmpty() && textView1.text.toString() != Constants.DOT
                ) {
                    val lastIndex = textView1.text.toString().lastIndex
                    textView1.text = textView1.text.toString().substring(0, lastIndex)
                    resultText()
                } else {
                    tvResult.text = EMPTY_STRING
                }
            }

            tvEqual.setOnClickListener {
                isDecimal = false
                tvResult.textSize = 50F
                equalClicked = true
                resultText()
                viewModel.addCalculation(
                    Calculation(
                        textView1.text.toString(),
                        tvResult.text.toString()
                    )
                )
            }

            viewHistoryIcon.setOnClickListener {
                this@CalculatorUI.showAnotherActivity(HistoryActivity::class.java)
            }
        }
    }

    /*Function to evaluate the expressions entered by user using NumPad*/

    private fun numberClick(input: String) {
        if (equalClicked) {
            equalClicked = false
            textView1.text = EMPTY_STRING
            tvResult.text = EMPTY_STRING
            textView1.append(input)
        } else {
            tvResult.text = EMPTY_STRING
            textView1.append(input)
        }
    }

    private fun operatorClick(operator: String) {
        if (equalClicked) {
            textView1.text = tvResult.text
            tvResult.text = EMPTY_STRING
            textView1.append(operator)
            equalClicked = false
        } else {
            tvResult.text = EMPTY_STRING
            textView1.append(operator)
        }
    }

    /* function to calculate result of the expression entered by user */

    private fun resultText() {
        if (getInputExpression().isNotEmpty()) {
            try {
                isDecimal = false
                var result = evaluateString(getInputExpression()).toString()
                if (!result.toDouble().isNaN()) {
                    if (!isDecimal) {
                        if (result.matches(divisionDecimal))
                            result = result.toDouble().toInt().toString()
                    }
                    tvResult.text = result
                } else {
                    tvResult.text = EMPTY_STRING
                    this@CalculatorUI.longToast("Wrong Format")
                }
            } catch (e: Exception) {
                textView1.text = textView1.text.toString()
                tvResult.text = EMPTY_STRING
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

    private fun evaluateString(input: String): Double {
        return if (input.contains(Constants.SUBTRACT) || input.contains(Constants.ADD)) {
            // Get all operands
            operands =
                input.split(Constants.ADD, Constants.SUBTRACT).toTypedArray()

            // Evaluate them from left to right
            var indexOperator: Int
            var operator: Char
            var operand: String
            operand = operands[0]
            var result: Double = evaluateString(operand)
            indexOperator = operand.length
            var counter = 1
            while (counter < operands.size) {
                operator = input[indexOperator]
                operand = operands[counter]
                if (operator == '+') result += evaluateString(operand) else result -= evaluateString(
                    operand
                )
                indexOperator += operand.length + 1
                counter++
            }
            result
        } else if (input.contains(Constants.MULTIPLY)) {
            val operands = input.split(Constants.MULTIPLY, limit = 2).toTypedArray()
            evaluateString(operands[0]) * evaluateString(operands[1])
        } else if (input.contains(Constants.DIVIDE)) {
            val operands = input.split(Constants.DIVIDE, limit = 2).toTypedArray()
            evaluateString(operands[0]) / evaluateString(operands[1])
        } else if (input.contains(Constants.MOD)) {
            val operands = input.split(Constants.MOD, limit = 2).toTypedArray()
            evaluateString(operands[0]) % evaluateString(operands[1])
        } else if (input.matches(re)) {
            input.toDouble()
        } else if (input.matches(reDecimal)) {
            isDecimal = true
            input.toDouble()
        } else {
            return Double.NaN
        }
    }
}