package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Statement

@Dao
interface StatementDao {
    @Query("SELECT * FROM statement")
    suspend fun getStatements(): List<Statement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatements(statements: List<Statement>)

    @Query("DELETE FROM statement")
    suspend fun deleteAll()
}