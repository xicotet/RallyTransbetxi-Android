package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Result

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(results: List<Result>)

    @Query("DELETE FROM result WHERE isGlobal = 1")
    suspend fun deleteGlobalResults()

    @Query("DELETE FROM result WHERE stageId = :stageId")
    suspend fun deleteStageResults(stageId: String)

    @Query("SELECT COUNT(*) FROM result WHERE isGlobal = 1")
    suspend fun countGlobalResults(): Int

    @Query("SELECT COUNT(*) FROM result WHERE stageId = :stageId")
    suspend fun countStageResults(stageId: String): Int

    @Query("SELECT * FROM result WHERE isGlobal = 1")
    suspend fun getGlobalResults(): List<Result>

    @Query("SELECT * FROM result WHERE stageId = :stageId")
    suspend fun getStageResults(stageId: String): List<Result>
}