package com.canolabs.rallytransbetxi.data.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.canolabs.rallytransbetxi.data.models.responses.AppSetting
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
import com.canolabs.rallytransbetxi.data.sources.local.database.AppDatabase
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DATABASE_NAME
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_ACTIVITIES_COLLAPSED
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_FONT_SIZE_FACTOR
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_NEWS_COLLAPSED
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_NOTIFICATION_PERMISSION_COUNTER
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_PROFILE
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_THEME
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_WARNINGS_COLLAPSED
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private lateinit var appDatabase: AppDatabase

    private val callback = object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // Dispatchers.IO is used because the callback is run on the main thread
            CoroutineScope(Dispatchers.IO).launch {
                val count = appDatabase.settingsDao().getSettingCount()
                if (count == 0) {
                    appDatabase.settingsDao().insertSetting(
                        AppSetting(
                            1,
                            DEFAULT_THEME,
                            DEFAULT_PROFILE,
                            DEFAULT_FONT_SIZE_FACTOR,
                            DEFAULT_NOTIFICATION_PERMISSION_COUNTER,
                            DEFAULT_WARNINGS_COLLAPSED,
                            DEFAULT_NEWS_COLLAPSED,
                            DEFAULT_ACTIVITIES_COLLAPSED
                        )
                    )
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        // First we check if the database is not initialized
        if (!::appDatabase.isInitialized) {
            appDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(callback)
                .build()
        }
        return appDatabase
    }

    @Provides
    fun provideSettingsDao(appDatabase: AppDatabase): AppSettingsDao {
        return appDatabase.settingsDao()
    }

    @Provides
    fun provideVersionsDao(appDatabase: AppDatabase): VersionsDao {
        return appDatabase.versionsDao()
    }

    @Provides
    fun provideStagesDao(appDatabase: AppDatabase): StagesDao {
        return appDatabase.stagesDao()
    }

    @Provides
    fun provideCategoriesDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoriesDao()
    }

    @Provides
    fun provideTeamsDao(appDatabase: AppDatabase): TeamDao {
        return appDatabase.teamsDao()
    }

    @Provides
    fun provideResultsDao(appDatabase: AppDatabase): ResultDao {
        return appDatabase.resultsDao()
    }

    @Provides
    fun provideActivitiesDao(appDatabase: AppDatabase): ActivityDao {
        return appDatabase.activitiesDao()
    }

    @Provides
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao {
        return appDatabase.newsDao()
    }

    @Provides
    fun provideHallOfFameDao(appDatabase: AppDatabase): HallOfFameDao {
        return appDatabase.hallOfFameDao()
    }

    @Provides
    fun provideRestaurantDao(appDatabase: AppDatabase): RestaurantDao {
        return appDatabase.restaurantDao()
    }

    @Provides
    fun provideStatementDao(appDatabase: AppDatabase): StatementDao {
        return appDatabase.statementsDao()
    }

    @Provides
    fun provideRaceWarningDao(appDatabase: AppDatabase): RaceWarningDao {
        return appDatabase.raceWarningDao()
    }
}