package com.example.nickelfoxassignment.newsapp.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article

@Dao
interface NewsDao {

    @Query("SELECT * FROM article_table where category = :category")
    fun getTopHeadlines(category: String): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArticles(images: List<Article>)

    @Query("DELETE FROM article_table where category = :category")
    suspend fun deleteArticles(category: String)
}