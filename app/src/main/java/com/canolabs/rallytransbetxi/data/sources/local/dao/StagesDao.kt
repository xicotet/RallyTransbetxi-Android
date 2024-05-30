package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Stage

@Dao
interface StagesDao {
     @Query("SELECT * FROM stage")
     suspend fun getStages(): List<Stage>

     @Query("SELECT * FROM stage WHERE acronym = :acronym")
     suspend fun getStage(acronym: String): Stage

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertStage(stage: Stage)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertStages(stages: List<Stage>)

     @Query("DELETE FROM stage")
     suspend fun deleteAllStages()
}