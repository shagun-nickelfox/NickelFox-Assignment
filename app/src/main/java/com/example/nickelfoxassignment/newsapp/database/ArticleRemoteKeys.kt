package com.example.nickelfoxassignment.newsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nickelfoxassignment.utils.Constants.ARTICLE_REMOTE_KEYS_TABLE

@Entity(tableName = ARTICLE_REMOTE_KEYS_TABLE)
data class ArticleRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val category: String?,
    val prev: Int?,
    val next: Int?
)