package com.example.nickelfoxassignment.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.nickelfoxassignment.newsapp.database.NewsDatabase
import com.example.nickelfoxassignment.newsapp.paging.NewsRemoteMediator
import com.example.nickelfoxassignment.newsapp.paging.SearchPagingSource
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class NewsRepository @Inject constructor(
    private val newsInterface: NewsInterface,
    private val newsDatabase: NewsDatabase
) {
    fun getTopHeadlines(category:String,chip:String): Flow<PagingData<Article>> {
        val pagingSourceFactory = { newsDatabase.getNewsDao().getTopHeadlines(chip) }
        return Pager(
            config = PagingConfig(pageSize = 50),
            remoteMediator = NewsRemoteMediator(
                newsInterface = newsInterface,
                newsDatabase = newsDatabase,
                category,
                chip
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getAllSearchNewsStream(q: String?): LiveData<PagingData<Article>> = Pager(
        config = PagingConfig(
            50,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SearchPagingSource(newsInterface = newsInterface, q)
        }
    ).liveData
}