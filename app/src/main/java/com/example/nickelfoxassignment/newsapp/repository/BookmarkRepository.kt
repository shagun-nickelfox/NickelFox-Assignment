package com.example.nickelfoxassignment.newsapp.repository

import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.database.BookmarkDao

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    fun insert(bookmark: Bookmark): Long {
        return bookmarkDao.insert(bookmark)
    }

    fun delete(bookmark: Bookmark) {
        bookmarkDao.delete(bookmark)
    }
}