package com.canolabs.rallytransbetxi.shared.data.sources.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.canolabs.rallytransbetxi.shared.data.models.responses.Version
import com.canolabs.rallytransbetxi.shared.data.sources.local.dao.VersionsDao
import com.canolabs.rallytransbetxi.shared.data.sources.local.typeConverters.TimestampConverter

@Database(
    entities = [
        Version::class,
    ], version = 3
)
@TypeConverters(
    TimestampConverter::class,
)
@ConstructedBy(TestDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun versionsDao(): VersionsDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object TestDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>