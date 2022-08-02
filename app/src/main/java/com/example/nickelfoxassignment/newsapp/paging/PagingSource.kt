package com.example.nickelfoxassignment.newsapp.paging

import androidx.paging.PagingSource
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import retrofit2.HttpException
import java.io.IOException

const val STARTING_INDEX = 1

class NewsPagingSource(private val newsInterface: NewsInterface) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        val position = params.key ?: STARTING_INDEX

        return try {
            val data = newsInterface.getTopHeadlines("in", "business",
                Constants.API_KEY,position,params.loadSize)
            LoadResult.Page(
                data = data.articles,
                prevKey = if(params.key == STARTING_INDEX) null else position -1,
                nextKey = if(data.articles.isEmpty())null else position+1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }
}