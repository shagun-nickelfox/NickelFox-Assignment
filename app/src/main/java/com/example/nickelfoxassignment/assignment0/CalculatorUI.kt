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
    private var unaryOperator = true

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
                "Calculator UI",
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
                evaluateExpression("1", clear = true)
            }

            tvTwo.setOnClickListener {
                evaluateExpression("2", clear = true)
            }

            tvThree.setOnClickListener {
                evaluateExpression("3", clear = true)
            }
            tvFour.setOnClickListener {
                evaluateExpression("4", clear = true)
            }

            tvFive.setOnClickListener {
                evaluateExpression("5", clear = true)
            }

            tvSix.setOnClickListener {
                evaluateExpression("6", clear = true)
            }

            tvSeven.setOnClickListener {
                evaluateExpression("7", clear = true)
            }

            tvEight.setOnClickListener {
                evaluateExpression("8", clear = true)
            }

            tvNine.setOnClickListener {
                evaluateExpression("9", clear = true)
            }

            tvZero.setOnClickListener {
                evaluateExpression("0", clear = true)
            }

            tvDoubleZero.setOnClickListener {
                evaluateExpression("00", clear = true)
            }

            /* Trigonometric Functions */

            tvSin.setOnClickListener {
                evaluateExpression("sin ", clear = true)
            }

            tvCos.setOnClickListener {
                evaluateExpression("cos ", clear = true)
            }

            tvTan.setOnClickListener {
                evaluateExpression("tan ", clear = true)
            }

            /*Operators*/

            tvPlus.setOnClickListener {
                evaluateExpression("+", clear = true)
            }

            tvSubtract.setOnClickListener {
                evaluateExpression("-", clear = true)
            }

            tvMultiply.setOnClickListener {
                evaluateExpression("*", clear = true)
            }

            tvDivide.setOnClickListener {
                evaluateExpression("/", clear = true)
            }

            tvDot.setOnClickListener {
                evaluateExpression(".", clear = true)
            }

            tvPercent.setOnClickListener {
                evaluateExpression("%", clear = true)
            }

            tvPlusMinus.setOnClickListener {
                if (unaryOperator) {
                    textView1.text = "-" + textView1.text.toString()
                    unaryOperator = false
                } else {
                    unaryOperator = true
                }
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

    private fun evaluateExpression(string: String, clear: Boolean) {
        if (clear) {
            tvResult.text = ""
            textView1.append(string)
        } else {
            textView1.append(tvResult.text)
            textView1.append(string)
            tvResult.text = ""
        }
    }

    private fun getInputExpression(): String {
        var expression = textView1.text.replace(Regex("÷"), "/")
        expression = expression.replace(Regex("×"), "*")
        return expression
    }
}