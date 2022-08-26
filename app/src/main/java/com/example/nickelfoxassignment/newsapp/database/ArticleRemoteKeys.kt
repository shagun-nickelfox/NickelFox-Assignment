package com.example.nickelfoxassignment.newsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_remote_keys")
data class ArticleRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val category: String?,
    val prev: Int?,
    val next: Int?
)