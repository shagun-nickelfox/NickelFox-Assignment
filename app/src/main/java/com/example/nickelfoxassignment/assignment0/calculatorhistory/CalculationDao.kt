package com.example.nickelfoxassignment.assignment0.calculatorhistory

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CalculationDao {

    @Query("Select * from calculation_table order by id asc")
    fun getCalculations(): LiveData<List<Calculation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(calculation: Calculation)

    @Delete
    fun delete(calculation: Calculation)
}