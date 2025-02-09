package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepository

class GetNotificationPermissionCounterUseCase(
    private val appSettingsRepository: AppSettingsRepository
)  {
    suspend operator fun invoke(): Int {
        return appSettingsRepository.getNotificationPermissionCounter()
    }
}