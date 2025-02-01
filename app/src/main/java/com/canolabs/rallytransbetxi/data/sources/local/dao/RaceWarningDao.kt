package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.RaceWarning

@Dao
interface RaceWarningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRaceWarning(raceWarning: RaceWarning)

    @Query("DELETE FROM racewarning WHERE stageId IS NULL")
    suspend fun deleteGlobalRaceWarnings()

    @Query("DELETE FROM racewarning WHERE stageId = :stageId")
    suspend fun deleteStageRaceWarnings(stageId: String)

    @Query("SELECT * FROM racewarning WHERE stageId IS NULL")
    suspend fun getGlobalRaceWarnings(): List<RaceWarning>

    @Query("SELECT * FROM racewarning WHERE stageId = :stageId")
    suspend fun getStageRaceWarnings(stageId: String): List<RaceWarning>
}