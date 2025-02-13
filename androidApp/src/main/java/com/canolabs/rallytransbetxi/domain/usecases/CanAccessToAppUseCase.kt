package com.canolabs.rallytransbetxi.domain.usecases

import android.util.Log
import com.canolabs.rallytransbetxi.data.repositories.AppVersionRepository

class CanAccessToAppUseCase(
     private val appVersionRepository: AppVersionRepository
) {
    suspend operator fun invoke(): Boolean {
        val currentVersion = appVersionRepository.getCurrentVersion()
        val minAllowedVersion = appVersionRepository.getMinAllowedVersion()

        Log.d("CanAccessToAppUseCase", "Current version: $currentVersion")
        Log.d("CanAccessToAppUseCase", "Min allowed version: $minAllowedVersion")

        // Guarantee access if for some reason version fetch fails (i.e., returns [0, 0, 0])
        if (currentVersion == listOf(0, 0, 0) || minAllowedVersion == listOf(0, 0, 0)) {
            Log.w("CanAccessToAppUseCase", "Version fetch failed. Allowing access.")
            return true
        }

        for (i in currentVersion.indices) {
            if (currentVersion[i] != minAllowedVersion[i]) {
                return currentVersion[i] > minAllowedVersion[i]
            }
        }
        return true // If all parts are equal, allow access
    }
}