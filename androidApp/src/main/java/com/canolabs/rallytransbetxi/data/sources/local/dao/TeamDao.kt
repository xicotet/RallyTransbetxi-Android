package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Team

@Dao
interface TeamDao {
    @Query("SELECT * FROM team")
    suspend fun getTeams(): List<Team>

    @Query("SELECT * FROM team WHERE name = :name")
    suspend fun getTeamByName(name: String): Team

    @Query("SELECT * FROM team WHERE number = :number")
    suspend fun getTeamByNumber(number: String): Team

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<Team>)

    @Query("DELETE FROM team")
    suspend fun deleteAllTeams()
}