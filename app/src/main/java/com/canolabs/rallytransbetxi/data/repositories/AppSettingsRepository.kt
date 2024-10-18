package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.AppSetting
import com.canolabs.rallytransbetxi.data.sources.local.dao.AppSettingsDao
import kotlinx.coroutines.delay
import javax.inject.Inject

interface AppSettingsRepository {
    suspend fun insertSetting(
        theme: String,
        profile: String,
        fontSizeFactor: Float,
        areWarningsCollapsed: Boolean,
        areNewsCollapsed: Boolean,
        areActivitiesCollapsed: Boolean
    )
    suspend fun getTheme(): String
    suspend fun getProfile(): String
    suspend fun getFontSizeFactor(): Float
    suspend fun getAreWarningsCollapsed(): Boolean
    suspend fun getAreNewsCollapsed(): Boolean
    suspend fun getAreActivitiesCollapsed(): Boolean
    suspend fun isDatabaseInitialized(): Boolean
}

class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsDao: AppSettingsDao
)  : AppSettingsRepository {

    override suspend fun insertSetting(
        theme: String,
        profile: String,
        fontSizeFactor: Float,
        areWarningsCollapsed: Boolean,
        areNewsCollapsed: Boolean,
        areActivitiesCollapsed: Boolean
    ) {
        appSettingsDao.insertSetting(
            AppSetting(
                1,
                theme,
                profile,
                fontSizeFactor,
                areWarningsCollapsed,
                areNewsCollapsed,
                areActivitiesCollapsed
            )
        )
    }

    override suspend fun getTheme(): String {
        while (!isDatabaseInitialized()) {
            delay(500)
        }
        return appSettingsDao.getTheme()
    }

    override suspend fun getProfile(): String {
        while (!isDatabaseInitialized()) {
            delay(500)
        }
        return appSettingsDao.getProfile()
    }

    override suspend fun getFontSizeFactor(): Float {
        while (!isDatabaseInitialized()) {
            delay(500)
        }
        return appSettingsDao.getFontSizeFactor()
    }

    override suspend fun getAreWarningsCollapsed(): Boolean {
        while (!isDatabaseInitialized()) {
            delay(500)
        }
        return appSettingsDao.getAreWarningsCollapsed()
    }

    override suspend fun getAreNewsCollapsed(): Boolean {
        while (!isDatabaseInitialized()) {
            delay(500)
        }
        return appSettingsDao.getAreNewsCollapsed()
    }

    override suspend fun getAreActivitiesCollapsed(): Boolean {
        while (!isDatabaseInitialized()) {
            delay(500)
        }
        return appSettingsDao.getAreActivitiesCollapsed()
    }

    override suspend fun isDatabaseInitialized(): Boolean {
        return appSettingsDao.getSettingCount() > 0
    }
}