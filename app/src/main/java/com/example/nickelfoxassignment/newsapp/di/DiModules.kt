package com.example.nickelfoxassignment.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.nickelfoxassignment.newsapp.database.BookmarkDao
import com.example.nickelfoxassignment.newsapp.database.BookmarkDatabase
import com.example.nickelfoxassignment.newsapp.database.NewsDatabase
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModules {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideNewsInterface(retrofit: Retrofit): NewsInterface {
        return retrofit.create(NewsInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): BookmarkDatabase {
        return Room.databaseBuilder(
            appContext,
            BookmarkDatabase::class.java,
            "bookmark_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideBookmarkDao(appDatabase: BookmarkDatabase): BookmarkDao {
        return appDatabase.getBookmarkDao()
    }

    /*@Singleton
    @Provides
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao {
        return newsDatabase.getNewsDao()
    }

    @Singleton
    @Provides
    fun provideNewsRepository(newsInterface: NewsInterface,newsDao: NewsDao): NewsRepository {
        return NewsRepository(newsInterface,newsDao)
    }*/
}