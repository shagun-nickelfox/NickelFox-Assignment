package com.example.nickelfoxassignment.newsapp.retrofit.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nickelfoxassignment.utils.Constants.ARTICLE_TABLE
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = ARTICLE_TABLE)
data class Article(
    @SerializedName("author") var author: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("publishedAt") var publishedAt: String?,
    @SerializedName("source") val source: Source,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    var category: String?
) : Parcelable {
    val id: String
        get() = UUID.randomUUID().toString()
}