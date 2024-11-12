package com.canolabs.rallytransbetxi.domain.usecases

import android.util.Log
import com.canolabs.rallytransbetxi.data.repositories.AppVersionRepositoryImpl
import javax.inject.Inject

class CanAccessToAppUseCase @Inject constructor(
     private val appVersionRepositoryImpl: AppVersionRepositoryImpl
) {
    suspend operator fun invoke(): Boolean {
        val currentVersion = appVersionRepositoryImpl.getCurrentVersion()
        val minAllowedVersion = appVersionRepositoryImpl.getMinAllowedVersion()

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