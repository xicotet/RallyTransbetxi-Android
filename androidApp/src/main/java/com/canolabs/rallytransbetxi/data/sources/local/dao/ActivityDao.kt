package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Activity

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity")
    suspend fun getAll(): List<Activity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<Activity>)

    @Query("DELETE FROM activity")
    suspend fun deleteAll()
}