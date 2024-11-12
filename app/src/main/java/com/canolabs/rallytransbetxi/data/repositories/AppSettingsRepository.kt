package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.AppSetting
import com.canolabs.rallytransbetxi.data.sources.local.dao.AppSettingsDao
import kotlinx.coroutines.delay
import javax.inject.Inject

interface AppSettingsRepository {
    suspend fun insertSetting(
        theme: String,
        profile: String,
        fontSizeFactor: Float
    )
    suspend fun getTheme(): String
    suspend fun getProfile(): String
    suspend fun getFontSizeFactor(): Float
    suspend fun isDatabaseInitialized(): Boolean
}

class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsDao: AppSettingsDao
)  : AppSettingsRepository {

    override suspend fun insertSetting(theme: String, profile: String, fontSizeFactor: Float) {
        appSettingsDao.insertSetting(AppSetting(1, theme, profile, fontSizeFactor))
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

    override suspend fun isDatabaseInitialized(): Boolean {
        return appSettingsDao.getSettingCount() > 0
    }
}