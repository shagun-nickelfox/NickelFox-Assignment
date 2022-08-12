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
    private var chipValue : String = "ForYou"

    fun setCategoryValue(category: String) {
        categoryInput.value = category
    }

    fun setChipValue(chip: String) {
        chipValue = chip
    }

    val list: LiveData<PagingData<Article>> = categoryInput.switchMap {
        newsRepository.getAllNewsStream(category = it,chipValue).cachedIn(viewModelScope)
    }
}