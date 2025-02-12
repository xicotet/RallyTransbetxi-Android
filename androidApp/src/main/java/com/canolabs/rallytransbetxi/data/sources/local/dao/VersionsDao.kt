package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Version
import dev.gitlive.firebase.firestore.Timestamp

@Dao
interface VersionsDao {
    @Query("SELECT timestamp FROM version WHERE name = :name")
    suspend fun getVersion(name: String): Timestamp

    @Query("SELECT COUNT(*) FROM version WHERE name = :name")
    suspend fun countVersionsByName(name: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVersion(version: Version)

    @Query("DELETE FROM version WHERE name = :name")
    suspend fun deleteVersion(name: String)
}