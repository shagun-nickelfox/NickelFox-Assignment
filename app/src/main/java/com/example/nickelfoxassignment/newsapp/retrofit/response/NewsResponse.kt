package com.example.nickelfoxassignment.newsapp.retrofit.response

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)