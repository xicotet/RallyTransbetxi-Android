package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepository

class GetAreActivitiesCollapsedUseCase(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(): Boolean {
        return appSettingsRepository.getAreActivitiesCollapsed()
    }
}