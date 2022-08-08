package com.example.nickelfoxassignment.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.database.BookmarkDatabase
import com.example.nickelfoxassignment.newsapp.repository.BookmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private var insertedId: Long = 0
    val allBookmarkNews: LiveData<List<Bookmark>>
    private val repository: BookmarkRepository

    init {
        val dao = BookmarkDatabase.getDatabase(application).getBookmarkDao()
        repository = BookmarkRepository(dao)
        allBookmarkNews = repository.allBookmark
    }

    fun addBookmark(bookmark: Bookmark) = viewModelScope.launch(Dispatchers.IO) {
        insertedId = repository.insert(bookmark)
    }

    fun getId(): Long {
        return insertedId
    }

    fun deleteBookmark(bookmark: Bookmark) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(bookmark)
    }
}