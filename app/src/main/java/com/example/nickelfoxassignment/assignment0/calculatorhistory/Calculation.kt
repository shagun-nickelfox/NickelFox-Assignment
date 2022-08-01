package com.example.nickelfoxassignment.assignment0.calculatorhistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_table")
class Calculation(
    @ColumnInfo(name = "inputText") val input: String,
    @ColumnInfo(name = "result") val result: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}