package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Warning

@Dao
interface WarningDao {
    @Query("SELECT * FROM warning")
    suspend fun getWarnings(): List<Warning>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarnings(warnings: List<Warning>)

    @Query("DELETE FROM warning")
    suspend fun deleteAll()
}