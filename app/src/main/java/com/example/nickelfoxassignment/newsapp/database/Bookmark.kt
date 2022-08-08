package com.example.nickelfoxassignment.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_table", indices = [Index(value = ["title"], unique = true)])
class Bookmark(
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "url") val url: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}