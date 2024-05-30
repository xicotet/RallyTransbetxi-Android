package com.canolabs.rallytransbetxi.data.sources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.canolabs.rallytransbetxi.data.models.responses.Version
import com.canolabs.rallytransbetxi.data.models.responses.AppSetting
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.sources.local.dao.AppSettingsDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.StagesDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.VersionsDao
import com.canolabs.rallytransbetxi.data.sources.local.typeConverters.GeoPointListConverter
import com.canolabs.rallytransbetxi.data.sources.local.typeConverters.TimestampConverter

@Database(
    entities = [
        AppSetting::class,
        Version::class,
        Stage::class
    ], version = 1
)
@TypeConverters(
    TimestampConverter::class,
    GeoPointListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): AppSettingsDao
    abstract fun versionsDao(): VersionsDao
    abstract fun stagesDao(): StagesDao
}