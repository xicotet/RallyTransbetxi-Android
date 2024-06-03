package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame

@Dao
interface HallOfFameDao {
    @Query("SELECT * FROM hallOfFame")
    suspend fun getAll(): List<HallOfFame>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(hallOfFame: List<HallOfFame>)

    @Query("DELETE FROM hallOfFame")
    suspend fun deleteAll()
}