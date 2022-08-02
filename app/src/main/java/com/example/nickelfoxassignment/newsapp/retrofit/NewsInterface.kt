package com.example.nickelfoxassignment.newsapp.retrofit

import com.example.nickelfoxassignment.newsapp.retrofit.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey : String,
        @Query("page") page : Int,
        @Query("pageSize") pageSize : Int,
    ): NewsResponse

    @GET("everything")
    suspend fun getAllNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey : String,
        @Query("page") page : Int,
        @Query("pageSize") pageSize : Int,
    ): NewsResponse
}