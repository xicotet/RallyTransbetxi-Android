package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepository
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile

class GetProfileSettingsUseCase(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(): DirectionsProfile {
        val profileString = appSettingsRepository.getProfile()
        return DirectionsProfile.entries.first { it.getDatabaseName() == profileString }
    }
}