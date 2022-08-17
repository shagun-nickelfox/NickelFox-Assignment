package com.example.nickelfoxassignment.newsapp.retrofit.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Article(
    var author: String?,
    val content: String,
    val description: String?,
    var publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
):Parcelable{
    val id = UUID.randomUUID()
}