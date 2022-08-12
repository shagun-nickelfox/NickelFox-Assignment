package com.example.nickelfoxassignment.newsapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.R
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val articleClickInterface: ArticleClickInterface,
    private val moreOptionsClickInterface: MoreOptionsClickInterface
) :
    PagingDataAdapter<Article, NewsAdapter.MyViewHolder>(DIFF_UTIL) {

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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)

        if (item != null) {
            holder.itemView.tvHeadline.text = item.title
            holder.itemView.tvNewsAuthor.text = item.author
            holder.itemView.tvNewsCategory.text = item.source?.name ?: ""
            val date = item.publishedAt?.let { getDateTimeDifference(it) }
            if (date != null) {
                if (date.days.toInt() == 0) {
                    holder.itemView.tvNewsTime.text = "${date.hours} hours ago"
                }
            }
            if (date != null) {
                if (date.hours.toInt() == 0) {
                    holder.itemView.tvNewsTime.text = "${date.minutes} minutes ago"
                } else if (date.minutes.toInt() == 0) {
                    holder.itemView.tvNewsTime.text = "${date.seconds} seconds ago"
                }
            }
            Glide.with(holder.itemView).load(item.urlToImage).apply(options)
                .into(holder.itemView.ivHeadline)

            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", item.title)
                bundle.putString("author", item.author)
                bundle.putString("source", item.source?.name)
                bundle.putString("time", holder.itemView.tvNewsTime.text.toString())
                bundle.putString("description", item.description)
                bundle.putString("image", item.urlToImage)
                bundle.putString("url", item.url)

                articleClickInterface.articleClick(bundle)
            }

            holder.itemView.tvMoreOptions.setOnClickListener {
                moreOptionsClickInterface.moreOptionsClick(
                    item,
                    holder.itemView.tvNewsTime.text.toString(),
                    holder.itemView.tvMoreOptions
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    private fun getDateTimeDifference(before: String): DateDifference {

        val parser =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()) // the initial pattern
        val now = Date().time
        // Put the time (in millis) in our formatter
        val formattedNow = parser.format(now)
        val fromParse = parser.parse(before)!!
        val toParse = parser.parse(formattedNow)!!

        var diff = fromParse.time - toParse.time

        // Validation
        if (diff < 0) diff *= -1

        // Because the difference is in the form of milliseconds, we should divide it first.
        val days = diff / (24 * 60 * 60 * 1000)
        // Get the remainder
        diff %= (24 * 60 * 60 * 1000)

        // Do this again as necessary.
        val hours = diff / (60 * 60 * 1000)
        diff %= (60 * 60 * 1000)
        val minutes = diff / (60 * 1000)
        diff %= (60 * 1000)
        val seconds = diff / 1000

        return DateDifference(
            days = days,
            hours = hours,
            minutes,
            seconds
        )

    }

    data class DateDifference(
        val days: Long,
        val hours: Long,
        val minutes: Long,
        val seconds: Long
    )

}

interface ArticleClickInterface {
    fun articleClick(bundle: Bundle)
}

interface MoreOptionsClickInterface {
    fun moreOptionsClick(article: Article, time: String, view: View)
}