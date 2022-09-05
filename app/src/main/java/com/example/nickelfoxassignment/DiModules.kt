package com.example.nickelfoxassignment

import android.content.Context
import androidx.room.Room
import com.example.nickelfoxassignment.Constants.BOOKMARK_DATABASE
import com.example.nickelfoxassignment.Constants.NEWS_DATABASE
import com.example.nickelfoxassignment.imageuploadapp.api.ImgurApi
import com.example.nickelfoxassignment.newsapp.database.BookmarkDao
import com.example.nickelfoxassignment.newsapp.database.BookmarkDatabase
import com.example.nickelfoxassignment.newsapp.database.NewsDatabase
import com.example.nickelfoxassignment.newsapp.retrofit.NewsInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModules {

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Singleton
    @Provides
    @Named("retrofit_1")
    fun provideRetrofit1(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("retrofit_2")
    fun provideRetrofit2(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.UPLOAD_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsInterface(@Named("retrofit_1") retrofit: Retrofit): NewsInterface {
        return retrofit.create(NewsInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            NEWS_DATABASE
        ).build()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): BookmarkDatabase {
        return Room.databaseBuilder(
            appContext,
            BookmarkDatabase::class.java,
            BOOKMARK_DATABASE
        ).build()
    }

    @Singleton
    @Provides
    fun provideBookmarkDao(appDatabase: BookmarkDatabase): BookmarkDao {
        return appDatabase.getBookmarkDao()
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }


    @Singleton
    @Provides
    fun provideImgurApiInterface(@Named("retrofit_2") retrofit: Retrofit): ImgurApi {
        return retrofit.create(ImgurApi::class.java)
    }
}