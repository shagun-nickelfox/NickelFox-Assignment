package com.example.nickelfoxassignment.assignment0.calculatorhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nickelfoxassignment.assignment0.CalculatorUI
import com.example.nickelfoxassignment.databinding.ActivityHistoryBinding
import com.example.nickelfoxassignment.utils.shortToast

class HistoryActivity : AppCompatActivity(), CalculationClickInterface, CalculationDeleteInterface {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: CalculationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val calculationAdapter = CalculationAdapter(this, this, this)
        binding.recyclerview.adapter = calculationAdapter
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CalculationViewModel::class.java]
        viewModel.allCalculation.observe(this) { list ->
            list?.let {
                calculationAdapter.updateList(it)
            }
        }
    }

    override fun calculationClick(calculation: Calculation) {
        val intent = Intent(this@HistoryActivity, CalculatorUI::class.java)
        intent.putExtra("calculationType", "Edit")
        intent.putExtra("calculationInput", calculation.input)
        intent.putExtra("calculationResult", calculation.result)
        intent.putExtra("calculationID", calculation.id)
        startActivity(intent)
        this.finish()
    }

    override fun calculationDelete(calculation: Calculation) {
        viewModel.deleteCalculation(calculation)
        this@HistoryActivity.shortToast("Deleted")
    }


}