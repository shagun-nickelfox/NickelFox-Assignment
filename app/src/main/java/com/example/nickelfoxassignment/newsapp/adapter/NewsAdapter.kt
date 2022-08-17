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
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val articleClickInterface: ArticleClickInterface,
    private val moreOptionsClickInterface: MoreOptionsClickInterface,
) :
    PagingDataAdapter<Article, NewsAdapter.MyViewHolder>(DiffUtil()) {
    private lateinit var calculatedDate: String

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)

        if (item != null) {
            val date = item.publishedAt?.let { getDateTimeDifference(it) }
            if (date != null) {
                calculatedDate = if (date.days.toInt() != 0) {
                    "${date.days}days ago"
                } else if (date.hours.toInt() != 0) {
                    "${date.hours}hr ago"
                } else if (date.minutes.toInt() != 0) {
                    "${date.minutes}m ago"
                } else {
                    "${date.seconds}s ago"
                }
            }

            val resources = holder.itemView.resources.getString(
                R.string.source_time,
                item.source?.name,
                calculatedDate
            )
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

            if (item.author == null)
                item.author = "Anonymous"
            val author = holder.itemView.resources.getString(
                R.string.news_author,
                item.author
            )

            holder.itemView.tvHeadline.text = item.title
            holder.itemView.tvNewsAuthor.text = author
            holder.itemView.tvNewsCategory.text = spannable
            Glide.with(holder.itemView).load(item.urlToImage).apply(options)
                .into(holder.itemView.ivHeadline)

            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", item.title)
                bundle.putString("author", item.author)
                bundle.putString("source", item.source?.name)
                bundle.putString("time", calculatedDate)
                bundle.putString("description", item.description)
                bundle.putString("image", item.urlToImage)
                bundle.putString("url", item.url)

                articleClickInterface.articleClick(bundle)
            }

            holder.itemView.tvMoreOptions.setOnClickListener {
                moreOptionsClickInterface.moreOptionsClick(
                    item,
                    calculatedDate,
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