package com.example.nickelfoxassignment

import android.content.Context
import androidx.room.Room
import com.example.nickelfoxassignment.utils.Constants.BOOKMARK_DATABASE
import com.example.nickelfoxassignment.utils.Constants.NEWS_DATABASE
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
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DiModules {

    @Qualifier
    annotation class NewsRetrofit

    @Qualifier
    annotation class UploadImageRetrofit

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Client-ID ${BuildConfig.CLIENT_ID}")
                    .build()
                chain.proceed(request)
            })
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.apply {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClient.addInterceptor(loggingInterceptor).build()
        } else okHttpClient
            .build()
    }

    @Singleton
    @Provides
    @NewsRetrofit
    fun provideNewsRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @UploadImageRetrofit
    fun provideImageUploadRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.UPLOAD_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsInterface(@NewsRetrofit retrofit: Retrofit): NewsInterface {
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
    fun provideImgurApiInterface(@UploadImageRetrofit retrofit: Retrofit): ImgurApi {
        return retrofit.create(ImgurApi::class.java)
    }
}