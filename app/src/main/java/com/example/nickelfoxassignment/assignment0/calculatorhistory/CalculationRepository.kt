package com.example.nickelfoxassignment.assignment0.calculatorhistory

import androidx.lifecycle.LiveData

class CalculationRepository(private val calculationDao: CalculationDao) {

    val allCalculation: LiveData<List<Calculation>> = calculationDao.getCalculations()

    fun insert(calculation: Calculation) {
        calculationDao.insert(calculation)
    }

    fun delete(calculation: Calculation) {
        calculationDao.delete(calculation)
    }
}