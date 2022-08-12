package com.example.nickelfoxassignment.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.nickelfoxassignment.newsapp.paging.NETWORK_PAGE_SIZE
import com.example.nickelfoxassignment.newsapp.paging.NewsPagingSource
import com.example.nickelfoxassignment.newsapp.paging.SearchPagingSource
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article

class NewsRepository(private val newsInterface: NewsInterface) {
    fun getAllNewsStream(category: String?,chip: String?): LiveData<PagingData<Article>> = Pager(
        config = PagingConfig(
            NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            NewsPagingSource(newsInterface, category, chip)
        }
    ).liveData

    fun getAllSearchNewsStream(q: String?): LiveData<PagingData<Article>> = Pager(
        config = PagingConfig(
            NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SearchPagingSource(newsInterface, q)
        }
    ).liveData
}