package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.storage.AppSetting

@Dao
interface AppSettingsDao {
    @Query("SELECT language FROM appsetting WHERE id = 1")
    suspend fun getLanguage(): String

    @Query("SELECT theme FROM appsetting WHERE id = 1")
    suspend fun getTheme(): String

    @Query("SELECT profile FROM appsetting WHERE id = 1")
    suspend fun getProfile(): String

    @Query("SELECT COUNT(*) FROM appsetting")
    suspend fun getSettingCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: AppSetting)
}