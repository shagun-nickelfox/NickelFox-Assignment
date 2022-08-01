package com.example.nickelfoxassignment.assignment0.calculatorhistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalculationViewModel(application: Application) : AndroidViewModel(application) {

    val allCalculation: LiveData<List<Calculation>>
    private val repository: CalculationRepository

    init {
        val dao = CalculationDatabase.getDatabase(application).getCalculationDao()
        repository = CalculationRepository(dao)
        allCalculation = repository.allCalculation
    }

    fun addCalculation(calculation: Calculation) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(calculation)
    }

    fun deleteCalculation(calculation: Calculation) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(calculation)
    }
}