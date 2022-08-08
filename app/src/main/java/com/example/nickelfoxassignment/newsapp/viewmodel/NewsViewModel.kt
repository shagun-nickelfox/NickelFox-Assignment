package com.example.nickelfoxassignment.newsapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.nickelfoxassignment.newsapp.repository.NewsRepository
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    newsRepository: NewsRepository,
) : ViewModel() {

    private val categoryInput = MutableLiveData("")
    private val searchInput = MutableLiveData("")

    fun setSearchValue(query: String) {
        searchInput.value = query
    }

    fun setCategoryValue(category: String) {
        categoryInput.value = category
    }

    val list: LiveData<PagingData<Article>> = categoryInput.switchMap {
        newsRepository.getAllNewsStream(category = it).cachedIn(viewModelScope)
    }

    val searchList: LiveData<PagingData<Article>> = searchInput.switchMap {
        newsRepository.getAllSearchNewsStream(query = it).cachedIn(viewModelScope)
    }
}