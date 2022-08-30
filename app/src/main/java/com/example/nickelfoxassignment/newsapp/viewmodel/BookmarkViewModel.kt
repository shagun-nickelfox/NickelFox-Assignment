package com.example.nickelfoxassignment.newsapp.viewmodel

import androidx.lifecycle.*
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.database.BookmarkDao
import com.example.nickelfoxassignment.newsapp.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(bookmarkDao: BookmarkDao) : ViewModel() {

    private var insertedId: Long = 0
    private val repository: BookmarkRepository
    private var categoryInput = MutableLiveData("For You")

    init {
        repository = BookmarkRepository(bookmarkDao)
    }

    fun setCategory(categoryValue: String) {
        categoryInput.value = categoryValue
    }

    val allBookmark: LiveData<List<Bookmark>> = categoryInput.switchMap {
        bookmarkDao.getBookmarkNews(it)
    }

    fun addBookmark(bookmark: Bookmark) = viewModelScope.launch(Dispatchers.IO) {
        insertedId = repository.insert(bookmark)
    }

    fun deleteBookmark(bookmark: Bookmark) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(bookmark)
    }

    fun exists(
        author: String?,
        title: String,
        source: String?
    ): LiveData<Boolean> {
        return repository.exists(author, title, source)
    }
}