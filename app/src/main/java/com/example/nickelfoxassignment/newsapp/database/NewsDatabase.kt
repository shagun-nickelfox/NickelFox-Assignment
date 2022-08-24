package com.example.nickelfoxassignment.newsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article

@Database(entities = [Article::class, ArticleRemoteKeys::class], version = 6, exportSchema = false)
@TypeConverters(SourceTypeConvertor::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}