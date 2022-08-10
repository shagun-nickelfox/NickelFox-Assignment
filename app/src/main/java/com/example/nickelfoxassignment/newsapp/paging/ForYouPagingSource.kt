package com.example.nickelfoxassignment.newsapp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.newsapp.retrofit.response.NewsResponse
import com.example.nickelfoxassignment.Constants
import retrofit2.HttpException
import java.io.IOException
import java.util.*

const val STARTING_FOR_INDEX = 1
const val NETWORK_FOR_PAGE_SIZE = 10

class ForYouPagingSource(
    private val newsInterface: NewsInterface
) : PagingSource<Int, Article>() {

    private lateinit var data: NewsResponse

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_FOR_INDEX
        return try {
            data = newsInterface.getTopHeadlines(
                Locale.getDefault().country,Locale.getDefault().language , "",
                Constants.API_KEY,position, params.loadSize
            )
            val repos = data.articles
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_FOR_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == STARTING_FOR_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}