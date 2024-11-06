package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Warning
import com.canolabs.rallytransbetxi.data.sources.local.dao.WarningDao
import com.canolabs.rallytransbetxi.data.sources.remote.WarningsServiceImpl
import javax.inject.Inject

interface WarningsRepository {
    suspend fun getWarnings(): List<Warning>
}

class WarningsRepositoryImpl @Inject constructor(
    private val warningsServiceImpl: WarningsServiceImpl,
    private val versionsRepositoryImpl: VersionsRepositoryImpl,
    private val warningsDao: WarningDao,
) : WarningsRepository {

    companion object {
        private const val TAG = "WarningsRepositoryImpl"
    }

    override suspend fun getWarnings(): List<Warning> {
        val versionName = "warnings"
        Log.d(TAG, "getWarnings() called")

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found. Fetching from API.")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName) ?: return emptyList()

            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val warnings = warningsServiceImpl.fetchWarnings()
            Log.d(TAG, "Fetched warnings from API: ${warnings.size} warnings")

            // The latest non-seen warning needs to be prompted as a dialog
            val shortedWarningsByDate = warnings.sortedByDescending { it.date }
            if (shortedWarningsByDate.isNotEmpty()) {
                shortedWarningsByDate.first().needsToBePromptedAsDialog = true
            }

            warningsDao.insertWarnings(shortedWarningsByDate)
            Log.d(TAG, "Inserted fetched warnings into local storage")

            return shortedWarningsByDate
        }

        // Get local and API versions
        val localVersion = versionsRepositoryImpl.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        // Compare versions
        return if (apiVersion != null && apiVersion != localVersion) {
            Log.d(TAG, "API version is different. Fetching data from API.")

            // If API version is newer, fetch from API and update local version
            val warnings = warningsServiceImpl.fetchWarnings()
            Log.d(TAG, "Fetched warnings from API: ${warnings.size} warnings")

            // The latest non-seen warning needs to be prompted as a dialog
            val shortedWarningsByDate = warnings.sortedByDescending { it.date }
            if (shortedWarningsByDate.isNotEmpty()) {
                shortedWarningsByDate.first().needsToBePromptedAsDialog = true
            }

            warningsDao.deleteAll()
            Log.d(TAG, "Deleted all local warnings")

            warningsDao.insertWarnings(shortedWarningsByDate)
            Log.d(TAG, "Inserted fetched warnings into local storage")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            shortedWarningsByDate
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localWarnings = warningsDao.getWarnings()
            Log.d(TAG, "Fetched warnings from local storage: ${localWarnings.size} warnings")

            // No new warnings, so no need to display the dialog
            localWarnings.map { it.needsToBePromptedAsDialog = false }

            localWarnings
        }
    }
}