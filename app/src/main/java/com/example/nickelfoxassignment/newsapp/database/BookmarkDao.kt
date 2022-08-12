package com.example.nickelfoxassignment.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {

    @Query("Select * from bookmark_table where category= :categoryValue order by id asc")
    fun getBookmarkNews(categoryValue: String): LiveData<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookmark: Bookmark): Long

    @Delete
    fun delete(bookmark: Bookmark)
}