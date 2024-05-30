package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.sources.local.dao.StagesDao
import com.canolabs.rallytransbetxi.data.sources.remote.StagesServiceImpl
import javax.inject.Inject

interface StagesRepository {
    suspend fun getStages(): List<Stage>
    suspend fun getStageByAcronym(acronym: String): Stage
}

class StagesRepositoryImpl @Inject constructor(
    private val stagesServiceImpl: StagesServiceImpl,
    private val versionsRepositoryImpl: VersionsRepositoryImpl,
    private val stagesDao: StagesDao
) : StagesRepository {

    companion object {
        private const val TAG = "StagesRepositoryImpl"
    }

    override suspend fun getStages(): List<Stage> {
        val versionName = "stages"
        Log.d(TAG, "getStages() called")

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found. Fetching from API.")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val stages = stagesServiceImpl.fetchStages()
            Log.d(TAG, "Fetched stages from API: ${stages.size} stages")

            stagesDao.insertStages(stages)
            Log.d(TAG, "Inserted fetched stages into local storage")

            return stages
        }

        // Get local and API versions
        val localVersion = versionsRepositoryImpl.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        // Compare versions
        return if (apiVersion > localVersion) {
            Log.d(TAG, "API version is newer. Fetching data from API.")

            // If API version is newer, fetch from API and update local version
            val stages = stagesServiceImpl.fetchStages()
            Log.d(TAG, "Fetched stages from API: ${stages.size} stages")

            stagesDao.deleteAllStages()
            Log.d(TAG, "Deleted all local stages")

            stagesDao.insertStages(stages)
            Log.d(TAG, "Inserted fetched stages into local storage")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            stages
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localStages = stagesDao.getStages()
            Log.d(TAG, "Fetched stages from local storage: ${localStages.size} stages")
            localStages
        }
    }

    override suspend fun getStageByAcronym(acronym: String): Stage {
        val versionName = "stages"
        Log.d(TAG, "getStageByAcronym() called with acronym: $acronym")

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found. Fetching from API.")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val stages = stagesServiceImpl.fetchStages()
            Log.d(TAG, "Fetched stages from API: ${stages.size} stages")

            stagesDao.insertStages(stages)
            Log.d(TAG, "Inserted fetched stages into local storage")

            return stagesDao.getStage(acronym)
        }

        // Get local and API versions
        val localVersion = versionsRepositoryImpl.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        // Compare versions
        return if (apiVersion > localVersion) {
            Log.d(TAG, "API version is newer. Fetching data from API.")

            // If API version is newer, fetch from API and update local version
            // If API version is newer, fetch from API and update local version
            val stages = stagesServiceImpl.fetchStages()
            Log.d(TAG, "Fetched stages from API: ${stages.size} stages")

            stagesDao.deleteAllStages()
            Log.d(TAG, "Deleted all local stages")

            stagesDao.insertStages(stages)
            Log.d(TAG, "Inserted fetched stages into local storage")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            return stagesDao.getStage(acronym)
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localStage = stagesDao.getStage(acronym)
            Log.d(TAG, "Fetched stage from local storage: $localStage")
            localStage
        }
    }
}