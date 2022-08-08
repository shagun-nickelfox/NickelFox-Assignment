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

const val STARTING_INDEX = 1
const val NETWORK_PAGE_SIZE = 10

class NewsPagingSource(
    private val newsInterface: NewsInterface,
    private val category: String?
) : PagingSource<Int, Article>() {

    private lateinit var data: NewsResponse

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_INDEX

        return try {
            data = newsInterface.getTopHeadlines(
                "in", category,
                Constants.API_KEY, params.loadSize
            )
            val repos = data.articles
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == STARTING_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}