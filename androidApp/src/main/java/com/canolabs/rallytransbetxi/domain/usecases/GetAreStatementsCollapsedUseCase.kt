package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepository

class GetAreStatementsCollapsedUseCase(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(): Boolean {
        return appSettingsRepository.getAreStatementsCollapsed()
    }
}