package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.storage.AppSetting
import com.canolabs.rallytransbetxi.data.sources.local.dao.AppSettingsDao
import javax.inject.Inject

interface AppSettingsRepository {
    suspend fun insertSetting(language: String, theme: String, profile: String)
    suspend fun getLanguage(): String
    suspend fun getTheme(): String
    suspend fun getProfile(): String
}

class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsDao: AppSettingsDao
)  : AppSettingsRepository {

    override suspend fun insertSetting(language: String, theme: String, profile: String) {
        appSettingsDao.insertSetting(AppSetting(1, language, theme, profile))
    }

    override suspend fun getLanguage(): String {
        return appSettingsDao.getLanguage()
    }

    override suspend fun getTheme(): String {
        return appSettingsDao.getTheme()
    }

    override suspend fun getProfile(): String {
        return appSettingsDao.getProfile()
    }
}