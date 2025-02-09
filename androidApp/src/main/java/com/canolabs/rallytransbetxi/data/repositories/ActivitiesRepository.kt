package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Activity
import com.canolabs.rallytransbetxi.data.sources.local.dao.ActivityDao
import com.canolabs.rallytransbetxi.data.sources.remote.ActivitiesService

interface ActivitiesRepository {
    suspend fun getActivities(): List<Activity>
}

class ActivitiesRepositoryImpl(
    private val activitiesService: ActivitiesService,
    private val versionsRepository: VersionsRepository,
    private val activitiesDao: ActivityDao,
) : ActivitiesRepository {

    companion object {
        private const val TAG = "ActivitiesRepositoryImpl"
    }

    override suspend fun getActivities(): List<Activity> {
        val versionName = "activities"
        Log.d(TAG, "getActivities() called")

        val localVersionCount = versionsRepository.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found.")
            val apiVersion = versionsRepository.getApiVersion(versionName) ?: return emptyList()
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val activities = activitiesService.fetchActivities()
            Log.d(TAG, "Fetched activities from API: ${activities.size} activities")

            activitiesDao.insertActivities(activities)
            Log.d(TAG, "Inserted fetched activities into local storage")

            return activities
        }

        // Get local and API versions
        val localVersion = versionsRepository.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepository.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        // Compare versions
        return if (apiVersion != null && apiVersion != localVersion) {
            Log.d(TAG, "API version is different. Fetching data from API.")

            // If API version is newer, fetch from API and update local version
            val activities = activitiesService.fetchActivities()
            Log.d(TAG, "Fetched activities from API: ${activities.size} activities")

            activitiesDao.deleteAll()
            Log.d(TAG, "Deleted all local activities")

            activitiesDao.insertActivities(activities)
            Log.d(TAG, "Inserted fetched activities into local storage")

            versionsRepository.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            activities
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localActivities = activitiesDao.getAll()
            Log.d(TAG, "Fetched activities from local storage: ${localActivities.size} activities")
            localActivities
        }
    }
}