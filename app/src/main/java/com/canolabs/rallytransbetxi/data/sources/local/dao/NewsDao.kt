package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.News

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    suspend fun getAll(): List<News>

    @Query("DELETE FROM news")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<News>)
}