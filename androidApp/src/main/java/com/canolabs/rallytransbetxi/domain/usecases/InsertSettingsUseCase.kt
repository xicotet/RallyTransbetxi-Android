package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepository

class InsertSettingsUseCase(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(
        theme: String,
        profile: String,
        fontSizeFactor: Float,
        notificationPermissionCounter: Int,
        areStatementsCollapsed: Boolean,
        areNewsCollapsed: Boolean,
        areActivitiesCollapsed: Boolean
    ) {
        appSettingsRepository.insertSetting(
            theme,
            profile,
            fontSizeFactor,
            notificationPermissionCounter,
            areStatementsCollapsed,
            areNewsCollapsed,
            areActivitiesCollapsed
        )
    }
}