package com.example.nickelfoxassignment.newsapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.R
import kotlinx.android.synthetic.main.list_item.view.*

class BookmarkAdapter(
    private val articleClickInterface: ArticleClickInterface,
    private val moreOptionsBookmarkClickInterface: MoreOptionsBookmarkClickInterface
) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    private val allBookmarkNews = ArrayList<Bookmark>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvHeadline.text = allBookmarkNews[position].title
        holder.itemView.tvNewsAuthor.text = allBookmarkNews[position].author
        holder.itemView.tvNewsCategory.text = allBookmarkNews[position].source
        holder.itemView.tvNewsTime.text = allBookmarkNews[position].time
        Glide.with(holder.itemView).load(allBookmarkNews[position].image)
            .into(holder.itemView.ivHeadline)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", allBookmarkNews[position].title)
            bundle.putString("author", allBookmarkNews[position].author)
            bundle.putString("source", allBookmarkNews[position].source)
            bundle.putString("time", allBookmarkNews[position].time)
            bundle.putString("description", allBookmarkNews[position].description)
            bundle.putString("image", allBookmarkNews[position].image)
            bundle.putString("url", allBookmarkNews[position].url)

            articleClickInterface.articleClick(bundle)
        }

        holder.itemView.tvMoreOptions.setOnClickListener {
            moreOptionsBookmarkClickInterface.moreOptionsBookmarkClick(allBookmarkNews[position],holder.itemView.tvMoreOptions)
        }
    }

    override fun getItemCount(): Int {
        return allBookmarkNews.size
    }

    fun updateList(newBookmark: List<Bookmark>) {
        allBookmarkNews.clear()
        allBookmarkNews.addAll(newBookmark)
        notifyDataSetChanged()
    }
}

interface MoreOptionsBookmarkClickInterface {
    fun moreOptionsBookmarkClick(bookmark: Bookmark,view: View)
}
