package com.canolabs.rallytransbetxi.data.sources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.canolabs.rallytransbetxi.data.models.storage.AppSetting
import com.canolabs.rallytransbetxi.data.sources.local.dao.AppSettingsDao

@Database(entities = [AppSetting::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): AppSettingsDao
}