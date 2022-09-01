package com.example.nickelfoxassignment.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.nickelfoxassignment.Constants.BOOKMARK_TABLE

@Entity(tableName = BOOKMARK_TABLE, indices = [Index(value = ["title"], unique = true)])
class Bookmark(
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "category") val category: String?,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: String
)