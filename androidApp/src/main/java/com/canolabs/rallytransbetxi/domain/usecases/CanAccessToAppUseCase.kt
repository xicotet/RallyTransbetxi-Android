package com.canolabs.rallytransbetxi.domain.usecases

import android.util.Log
import com.canolabs.rallytransbetxi.data.repositories.AppVersionRepository

class CanAccessToAppUseCase(
     private val appVersionRepository: AppVersionRepository
) {
    suspend operator fun invoke(): Boolean {
        val currentVersion = appVersionRepository.getCurrentVersion()
        val minAllowedVersion = appVersionRepository.getMinAllowedVersion()

        // Guarantee access if for some reason version fetch fails (i.e., returns [0, 0, 0])
        if (currentVersion == listOf(0, 0, 0) || minAllowedVersion == listOf(0, 0, 0)) {
            Log.w("CanAccessToAppUseCase", "Version fetch failed. Allowing access.")
            return true
        }

        return currentVersion.zip(minAllowedVersion).any { (currentPart, minPart) ->
            currentPart != minPart && currentPart > minPart
        }
    }
}