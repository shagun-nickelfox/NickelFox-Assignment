package com.example.nickelfoxassignment.assignment0.calculatorhistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nickelfoxassignment.R

class CalculationAdapter(
    val context: Context,
    private val calculationClickInterface: CalculationClickInterface,
    private val calculationClickDeleteInterface: CalculationDeleteInterface
) : RecyclerView.Adapter<CalculationAdapter.ViewHolder>() {

    private val allCalculation = ArrayList<Calculation>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inputText: TextView = itemView.findViewById(R.id.tvInput)
        val resultText: TextView = itemView.findViewById(R.id.tvResult)
        val deleteImage: ImageView = itemView.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.inputText.text = allCalculation[position].input
        holder.resultText.text = allCalculation[position].result
        holder.deleteImage.setOnClickListener {
            calculationClickDeleteInterface.calculationDelete(allCalculation[position])
        }
        holder.itemView.setOnClickListener {
            calculationClickInterface.calculationClick(allCalculation[position])
        }
    }

    override fun getItemCount(): Int {
        return allCalculation.size
    }

    fun updateList(newCalculation: List<Calculation>) {
        allCalculation.clear()
        allCalculation.addAll(newCalculation)
        notifyDataSetChanged()
    }

}

interface CalculationClickInterface {
    fun calculationClick(calculation: Calculation)
}

interface CalculationDeleteInterface {
    fun calculationDelete(calculation: Calculation)
}