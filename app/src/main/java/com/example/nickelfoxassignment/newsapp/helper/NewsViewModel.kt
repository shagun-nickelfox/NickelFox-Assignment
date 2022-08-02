package com.example.nickelfoxassignment.newsapp.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    newsRepository: NewsRepository
    ): ViewModel() {

        val list : LiveData<PagingData<Article>> = newsRepository.getAllNewsStream()
}