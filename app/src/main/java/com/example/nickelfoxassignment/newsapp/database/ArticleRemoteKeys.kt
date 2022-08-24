package com.example.nickelfoxassignment.newsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "article_remote_keys")
data class ArticleRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prev: Int?,
    val next: Int?
)