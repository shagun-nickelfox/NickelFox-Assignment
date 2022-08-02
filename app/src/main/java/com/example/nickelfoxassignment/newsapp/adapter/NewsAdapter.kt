package com.example.nickelfoxassignment.newsapp.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nickelfoxassignment.databinding.ListItemBinding
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import kotlinx.android.synthetic.main.list_item.view.*

class NewsAdapter : PagingDataAdapter<Article, NewsAdapter.MyViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }

    inner class MyViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root){

        private val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_delete)
            .error(R.drawable.ic_delete)

        fun bind(article: Article) {
            with(itemView) {
                binding.root.tvHeadline.text= article.title
                binding.root.tvAuthor.text = article.author
                Glide.with(binding.root).load(article.urlToImage).apply(options).into(binding.root.ivHeadline)
            }
        }
        }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

}