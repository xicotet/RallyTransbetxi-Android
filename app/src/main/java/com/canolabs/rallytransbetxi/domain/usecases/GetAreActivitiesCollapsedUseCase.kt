package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepositoryImpl
import javax.inject.Inject

class GetAreActivitiesCollapsedUseCase @Inject constructor(
    private val appSettingsRepositoryImpl: AppSettingsRepositoryImpl
) {
    suspend operator fun invoke(): Boolean {
        return appSettingsRepositoryImpl.getAreActivitiesCollapsed()
    }
}