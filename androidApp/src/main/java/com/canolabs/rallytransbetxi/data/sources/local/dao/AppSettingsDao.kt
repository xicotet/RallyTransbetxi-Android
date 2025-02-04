package com.canolabs.rallytransbetxi.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canolabs.rallytransbetxi.data.models.responses.AppSetting

@Dao
interface AppSettingsDao {
    @Query("SELECT theme FROM appsetting WHERE id = 1")
    suspend fun getTheme(): String

    @Query("SELECT profile FROM appsetting WHERE id = 1")
    suspend fun getProfile(): String

    @Query("SELECT COUNT(*) FROM appsetting")
    suspend fun getSettingCount(): Int

    @Query("SELECT fontSizeFactor FROM appsetting WHERE id = 1")
    suspend fun getFontSizeFactor(): Float

    @Query("SELECT notificationPermissionCounter FROM appsetting WHERE id = 1")
    suspend fun getNotificationPermissionCounter(): Int

    @Query("SELECT areStatementsCollapsed FROM appsetting WHERE id = 1")
    suspend fun getAreStatementsCollapsed(): Boolean

    @Query("SELECT areNewsCollapsed FROM appsetting WHERE id = 1")
    suspend fun getAreNewsCollapsed(): Boolean

    @Query("SELECT areActivitiesCollapsed FROM appsetting WHERE id = 1")
    suspend fun getAreActivitiesCollapsed(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: AppSetting)
}