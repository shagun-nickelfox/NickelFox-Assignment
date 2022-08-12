package com.example.nickelfoxassignment.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.database.BookmarkDao
import com.example.nickelfoxassignment.newsapp.database.BookmarkDatabase
import com.example.nickelfoxassignment.newsapp.repository.BookmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private var insertedId: Long = 0
    private val repository: BookmarkRepository
    private var dao: BookmarkDao
    private var categoryInput = MutableLiveData("For You")

    init {
        dao = BookmarkDatabase.getDatabase(application).getBookmarkDao()
        repository = BookmarkRepository(dao)
    }

    fun setCategory(categoryValue: String) {
        categoryInput.value = categoryValue
    }

    val allBookmark: LiveData<List<Bookmark>> = categoryInput.switchMap {
        dao.getBookmarkNews(it)
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