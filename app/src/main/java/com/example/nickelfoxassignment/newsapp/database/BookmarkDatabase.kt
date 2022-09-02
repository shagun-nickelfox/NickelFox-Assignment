package com.example.nickelfoxassignment.newsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class], version = 9, exportSchema = false)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun getBookmarkDao(): BookmarkDao
}