package com.example.nickelfoxassignment.newsapp.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article

@Dao
interface NewsDao {

    @Query("SELECT * FROM article_table where category = :category")
    fun getTopHeadlines(category: String): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImages(images: List<Article>)

    @Query("DELETE FROM article_table where category = :category")
    suspend fun deleteAllImages(category: String)
}