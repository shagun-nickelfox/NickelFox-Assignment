package com.example.nickelfoxassignment.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {

    @Query("Select * from bookmark_table where category= :categoryValue")
    fun getBookmarkNews(categoryValue: String): LiveData<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookmark: Bookmark): Long

    @Delete
    fun delete(bookmark: Bookmark)

    @Query("SELECT EXISTS (SELECT 1 FROM bookmark_table WHERE author = :author and title = :title and source = :source)")
    fun exists(author: String?, title: String, source: String?): Boolean
}