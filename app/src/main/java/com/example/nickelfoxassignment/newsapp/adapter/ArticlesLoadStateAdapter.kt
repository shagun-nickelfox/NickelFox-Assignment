package com.example.nickelfoxassignment.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.LoaderItemBinding

class ArticlesLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ArticlesLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LoaderItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class LoadStateViewHolder(private val binding: LoaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                pbLoading.isVisible = loadState is LoadState.Loading
                tvError.isVisible = loadState is LoadState.Error
                btnRetry.isVisible = loadState is LoadState.Error

                if (loadState is LoadState.Error) {
                    if (loadState.error.localizedMessage == itemView.resources
                            .getString(R.string.data_fetch_error)
                    ) {
                        tvError.text = itemView.resources
                            .getString(R.string.internet_error)
                    } else
                        tvError.text = itemView.resources
                            .getString(R.string.api_error)
                }

                btnRetry.setOnClickListener {
                    retry()
                }
            }
        }
    }
}