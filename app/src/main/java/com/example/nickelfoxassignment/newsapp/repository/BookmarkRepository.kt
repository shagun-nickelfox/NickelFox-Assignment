package com.example.nickelfoxassignment.newsapp.repository

import androidx.lifecycle.LiveData
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.database.BookmarkDao

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {
    val allBookmark: LiveData<List<Bookmark>> = bookmarkDao.getBookmarkNews()

    fun insert(bookmark: Bookmark): Long {
        return bookmarkDao.insert(bookmark)
    }

    fun delete(bookmark: Bookmark) {
        bookmarkDao.delete(bookmark)
    }
}