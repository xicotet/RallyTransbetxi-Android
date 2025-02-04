package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepositoryImpl
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import javax.inject.Inject

class GetProfileSettingsUseCase @Inject constructor(
    private val appSettingsRepositoryImpl: AppSettingsRepositoryImpl
) {
    suspend operator fun invoke(): DirectionsProfile {
        val profileString = appSettingsRepositoryImpl.getProfile()
        return DirectionsProfile.entries.first { it.getDatabaseName() == profileString }
    }
}