package com.example.nickelfoxassignment.newsapp.helper

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.nickelfoxassignment.newsapp.paging.NewsPagingSource
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article

class NewsRepository(private val newsInterface: NewsInterface) {

    fun getAllNewsStream():LiveData<PagingData<Article>> = Pager(
        config = PagingConfig(
            20,
            5,
            enablePlaceholders = false
        ),
        pagingSourceFactory ={
            NewsPagingSource(newsInterface)
        }
    ).liveData
}