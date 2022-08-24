package com.example.nickelfoxassignment.newsapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.nickelfoxassignment.newsapp.repository.NewsRepository
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    newsRepository: NewsRepository,
) : ViewModel() {

    private val searchInput = MutableLiveData("")

    fun setSearchValue(query: String) {
        searchInput.value = query
    }

    val searchList: LiveData<PagingData<Article>> = searchInput.switchMap { query->
            newsRepository.getAllSearchNewsStream(q = query).cachedIn(viewModelScope)
    }
}