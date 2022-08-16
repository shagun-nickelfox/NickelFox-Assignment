package com.example.nickelfoxassignment.newsapp.adapter

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.R
import kotlinx.android.synthetic.main.list_item.view.*

class BookmarkAdapter(
    private val articleClickInterface: ArticleClickInterface,
    private val moreOptionsBookmarkClickInterface: MoreOptionsBookmarkClickInterface
) : ListAdapter<Bookmark, BookmarkAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Bookmark>() {
            override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        val resources =
            holder.itemView.resources.getString(R.string.source_time, item.source, item.time)
        val len1 = resources.split(" • ")[0].length
        val spannable = SpannableString(resources)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(holder.itemView.context, R.color.blue)),
            0, len1,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0, // start
            len1, // end
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        holder.itemView.tvHeadline.text = item.title
        holder.itemView.tvNewsAuthor.text = "By ${item.author}"
        holder.itemView.tvNewsCategory.text = spannable
        Glide.with(holder.itemView).load(item.image)
            .into(holder.itemView.ivHeadline)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", item.title)
            bundle.putString("author", item.author)
            bundle.putString("source", item.source)
            bundle.putString("time", item.time)
            bundle.putString("description", item.description)
            bundle.putString("image", item.image)
            bundle.putString("url", item.url)

            articleClickInterface.articleClick(bundle)
        }

        holder.itemView.tvMoreOptions.setOnClickListener {
            moreOptionsBookmarkClickInterface.moreOptionsBookmarkClick(
                item,
                holder.itemView.tvMoreOptions
            )
        }
    }
}

interface MoreOptionsBookmarkClickInterface {
    fun moreOptionsBookmarkClick(bookmark: Bookmark, view: View)
}
