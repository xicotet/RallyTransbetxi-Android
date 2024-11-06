package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.AppVersionRepositoryImpl
import javax.inject.Inject

class CanAccessToAppUseCase @Inject constructor(
     private val appVersionRepositoryImpl: AppVersionRepositoryImpl
) {
    suspend operator fun invoke(): Boolean {
        val currentVersion = appVersionRepositoryImpl.getCurrentVersion()
        val minAllowedVersion = appVersionRepositoryImpl.getMinAllowedVersion()

        return currentVersion.zip(minAllowedVersion).any { (currentPart, minPart) ->
            currentPart != minPart && currentPart > minPart
        }
    }
}