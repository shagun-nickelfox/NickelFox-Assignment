package com.example.nickelfoxassignment.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {

    @Query("Select * from bookmark_table order by id asc")
    fun getBookmarkNews(): LiveData<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookmark: Bookmark): Long

    @Delete
    fun delete(bookmark: Bookmark)
}