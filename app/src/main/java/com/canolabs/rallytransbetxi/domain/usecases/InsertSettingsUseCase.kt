package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepositoryImpl
import javax.inject.Inject

class InsertSettingsUseCase @Inject constructor(
    private val appSettingsRepositoryImpl: AppSettingsRepositoryImpl
) {
    suspend operator fun invoke(
        theme: String,
        profile: String,
        fontSizeFactor: Float,
        areWarningsCollapsed: Boolean,
        areNewsCollapsed: Boolean,
        areActivitiesCollapsed: Boolean
    ) {
        appSettingsRepositoryImpl.insertSetting(
            theme,
            profile,
            fontSizeFactor,
            areWarningsCollapsed,
            areNewsCollapsed,
            areActivitiesCollapsed
        )
    }
}