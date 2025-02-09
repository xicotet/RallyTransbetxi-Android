package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepository

class GetThemeSettingsUseCase(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(): String {
        return appSettingsRepository.getTheme()
    }
}