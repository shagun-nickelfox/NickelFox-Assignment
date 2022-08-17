package com.example.nickelfoxassignment.newsapp.paging

import androidx.paging.PagingSource
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.newsapp.retrofit.response.NewsResponse
import com.example.nickelfoxassignment.Constants
import retrofit2.HttpException
import java.io.IOException
import java.util.*

const val STARTING_INDEX = 1
const val NETWORK_PAGE_SIZE = 10

class NewsPagingSource(
    private val newsInterface: NewsInterface,
    private val category: String?,
    private val chip: String?
) : PagingSource<Int, Article>() {

    private lateinit var data: NewsResponse

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_INDEX

        return try {
            data = newsInterface.getTopHeadlines(
                if (chip == "For You") Locale.getDefault().country else "",
                Locale.getDefault().language,
                category,
                Constants.API_KEY,
                position,
                params.loadSize
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