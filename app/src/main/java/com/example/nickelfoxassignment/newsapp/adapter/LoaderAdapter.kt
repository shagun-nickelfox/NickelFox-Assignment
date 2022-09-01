package com.example.nickelfoxassignment.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nickelfoxassignment.R
import kotlinx.android.synthetic.main.loader_item.view.*

class LoaderAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoaderAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        val progress = holder.itemView.pbLoading
        val btnRetry = holder.itemView.btnRetry
        val txtErrorMessage = holder.itemView.tvError

        btnRetry.isVisible = loadState is LoadState.Error
        txtErrorMessage.isVisible = loadState is LoadState.Error
        progress.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error) {
            txtErrorMessage.text = loadState.error.localizedMessage
        }

        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loader_item, parent, false)
        )
    }

    class LoadStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
}