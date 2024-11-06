package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame
import com.canolabs.rallytransbetxi.data.sources.local.dao.HallOfFameDao
import com.canolabs.rallytransbetxi.data.sources.remote.HallOfFameServiceImpl
import javax.inject.Inject

interface HallOfFameRepository {
    suspend fun getHallOfFame(): List<HallOfFame>
}

class HallOfFameRepositoryImpl @Inject constructor(
    private val hallOfFameServiceImpl: HallOfFameServiceImpl,
    private val versionsRepositoryImpl: VersionsRepositoryImpl,
    private val hallOfFameDao: HallOfFameDao,
) : HallOfFameRepository {

    companion object {
        private const val TAG = "HallOfFameRepositoryImpl"
    }

    override suspend fun getHallOfFame(): List<HallOfFame> {
        val versionName = "hall_of_fame"

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName) ?: return emptyList()
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val hallOfFame = hallOfFameServiceImpl.fetchHallOfFame()
            Log.d(TAG, "Fetched hall of fame from API: ${hallOfFame.size} entries")

            hallOfFameDao.insertAll(hallOfFame)
            Log.d(TAG, "Inserted fetched hall of fame entries into local storage")

            return hallOfFame
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
            val hallOfFame = hallOfFameServiceImpl.fetchHallOfFame()
            Log.d(TAG, "Fetched hall of fame from API: ${hallOfFame.size} entries")

            hallOfFameDao.deleteAll()
            Log.d(TAG, "Deleted all local hall of fame entries")

            hallOfFameDao.insertAll(hallOfFame)
            Log.d(TAG, "Inserted fetched hall of fame entries into local storage")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            hallOfFame
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localHallOfFame = hallOfFameDao.getAll()
            Log.d(TAG, "Fetched hall of fame from local storage: ${localHallOfFame.size} entries")
            localHallOfFame
        }
    }
}