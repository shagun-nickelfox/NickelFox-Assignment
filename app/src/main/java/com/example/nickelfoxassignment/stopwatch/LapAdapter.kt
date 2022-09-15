package com.example.nickelfoxassignment.stopwatch

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nickelfoxassignment.R
import kotlinx.android.synthetic.main.lap_list_item.view.*

class LapAdapter(private val context: Context) :
    RecyclerView.Adapter<LapAdapter.ViewHolder>() {
    private var list: ArrayList<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val myListItem = LayoutInflater.from(context)
            .inflate(R.layout.lap_list_item, parent, false)
        return ViewHolder(myListItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItemToList(item: String) {
        list.add(item)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(myItem: String, position: Int) {
            itemView.tvLapTimes.text = "#${position + 1}  " + myItem
        }
    }
}