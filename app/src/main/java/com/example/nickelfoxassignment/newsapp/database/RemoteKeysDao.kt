package com.example.nickelfoxassignment.newsapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM article_remote_keys WHERE id =:id")
    suspend fun getRemoteKeys(id: String): ArticleRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<ArticleRemoteKeys>)

    @Query("DELETE FROM article_remote_keys where category = :category")
    suspend fun deleteAllRemoteKeys(category: String)
}