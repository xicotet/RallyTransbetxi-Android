package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurant")
    fun getAll(): List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(restaurants: List<Restaurant>)

    @Query("DELETE FROM restaurant")
    fun deleteAll()
}