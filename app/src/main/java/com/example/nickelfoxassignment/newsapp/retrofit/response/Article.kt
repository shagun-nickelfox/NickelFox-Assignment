package com.example.nickelfoxassignment.newsapp.retrofit.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "article_table")
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
) : Parcelable {
    var category: String? = null
    var id: String? = null
}