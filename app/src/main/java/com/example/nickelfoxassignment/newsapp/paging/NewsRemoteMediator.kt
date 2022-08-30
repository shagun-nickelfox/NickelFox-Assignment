package com.example.nickelfoxassignment.newsapp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.nickelfoxassignment.Constants
import com.example.nickelfoxassignment.newsapp.database.ArticleRemoteKeys
import com.example.nickelfoxassignment.newsapp.database.NewsDatabase
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import java.util.*

@ExperimentalPagingApi
class NewsRemoteMediator(
    private val newsInterface: NewsInterface,
    private val newsDatabase: NewsDatabase,
    private val category: String,
    private val chip: String
) : RemoteMediator<Int, Article>() {
    private val newsDao = newsDatabase.getNewsDao()
    private val newsRemoteKeysDao = newsDatabase.getRemoteKeysDao()
    private var previousChip = ""

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.next?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prev
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.next
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response =
                newsInterface.getTopHeadlines(
                    if (chip == "For You") Locale.getDefault().country else "",
                    Locale.getDefault().language,
                    category,
                    Constants.API_KEY,
                    currentPage,
                    50
                )

            response.articles.onEach { article ->
                article.category = chip
            }

            val endOfPaginationReached = response.articles.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            newsDatabase.withTransaction {
                if (previousChip == chip) {
                    if (loadType == LoadType.REFRESH) {
                        newsDao.deleteArticles(chip)
                        newsRemoteKeysDao.deleteAllRemoteKeys(chip)
                    }
                }

                previousChip = chip

                val keys = response.articles.map { unsplashImage ->
                    ArticleRemoteKeys(
                        id = unsplashImage.title,
                        category = unsplashImage.category,
                        prev = prevPage,
                        next = nextPage
                    )
                }
                newsRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                newsDao.addArticles(response.articles)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): ArticleRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.title?.let { id ->
                newsRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Article>
    ): ArticleRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                newsRemoteKeysDao.getRemoteKeys(id = unsplashImage.title)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Article>
    ): ArticleRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                newsRemoteKeysDao.getRemoteKeys(id = unsplashImage.title)
            }
    }

}