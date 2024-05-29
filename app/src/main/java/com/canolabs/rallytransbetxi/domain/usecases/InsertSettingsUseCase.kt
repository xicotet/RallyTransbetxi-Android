package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepositoryImpl
import javax.inject.Inject

class InsertSettingsUseCase @Inject constructor(
    private val appSettingsRepositoryImpl: AppSettingsRepositoryImpl
) {
    suspend operator fun invoke(
        language: String,
        theme: String,
        profile: String,
        fontSizeFactor: Float
    ) {
        appSettingsRepositoryImpl.insertSetting(
            language,
            theme,
            profile,
            fontSizeFactor
        )
    }
}