package com.canolabs.rallytransbetxi.data.sources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.canolabs.rallytransbetxi.data.models.responses.Activity
import com.canolabs.rallytransbetxi.data.models.responses.Version
import com.canolabs.rallytransbetxi.data.models.responses.AppSetting
import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import com.canolabs.rallytransbetxi.data.models.responses.Statement
import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame
import com.canolabs.rallytransbetxi.data.models.responses.RaceWarning
import com.canolabs.rallytransbetxi.data.sources.local.dao.ActivityDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.AppSettingsDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.CategoryDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.HallOfFameDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.NewsDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.RaceWarningDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.RestaurantDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.ResultDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.StagesDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.TeamDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.VersionsDao
import com.canolabs.rallytransbetxi.data.sources.local.dao.StatementDao
import com.canolabs.rallytransbetxi.data.sources.local.typeConverters.CategoryTypeConverter
import com.canolabs.rallytransbetxi.data.sources.local.typeConverters.GeoPointConverter
import com.canolabs.rallytransbetxi.data.sources.local.typeConverters.GeoPointListConverter
import com.canolabs.rallytransbetxi.data.sources.local.typeConverters.TeamTypeConverter
import com.canolabs.rallytransbetxi.data.sources.local.typeConverters.TimestampConverter

@Database(
    entities = [
        AppSetting::class,
        Version::class,
        Stage::class,
        Category::class,
        Team::class,
        Result::class,
        Activity::class,
        News::class,
        HallOfFame::class,
        Restaurant::class,
        Statement::class,
        RaceWarning::class
    ], version = 2
)
@TypeConverters(
    TimestampConverter::class,
    GeoPointListConverter::class,
    GeoPointConverter::class,
    CategoryTypeConverter::class,
    TeamTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): AppSettingsDao
    abstract fun versionsDao(): VersionsDao
    abstract fun stagesDao(): StagesDao
    abstract fun categoriesDao(): CategoryDao
    abstract fun teamsDao(): TeamDao
    abstract fun resultsDao(): ResultDao
    abstract fun activitiesDao(): ActivityDao
    abstract fun newsDao(): NewsDao
    abstract fun hallOfFameDao(): HallOfFameDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun statementsDao(): StatementDao
    abstract fun raceWarningDao(): RaceWarningDao
}