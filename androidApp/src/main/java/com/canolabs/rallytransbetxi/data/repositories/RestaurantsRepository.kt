package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import com.canolabs.rallytransbetxi.data.sources.local.dao.RestaurantDao
import com.canolabs.rallytransbetxi.data.sources.remote.RestaurantsService

interface RestaurantsRepository {
    suspend fun getRestaurants(): List<Restaurant>
}

class RestaurantsRepositoryImpl(
    private val restaurantsService: RestaurantsService,
    private val restaurantsDao: RestaurantDao,
    private val versionsRepository: VersionsRepository
): RestaurantsRepository {

    companion object {
        private const val TAG = "RestaurantsRepositoryImpl"
    }

    override suspend fun getRestaurants(): List<Restaurant> {
        val versionName = "restaurants"
        Log.d(TAG, "getRestaurants() called")

        val localVersionCount = versionsRepository.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found")
            val apiVersion = versionsRepository.getApiVersion(versionName) ?: return emptyList()
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val restaurants = restaurantsService.fetchRestaurants()
            Log.d(TAG, "Fetched restaurants from API: ${restaurants.size} entries")

            restaurantsDao.insertAll(restaurants)
            Log.d(TAG, "Inserted fetched hall of fame entries into local storage")

            return restaurants
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
            val restaurants = restaurantsService.fetchRestaurants()
            Log.d(TAG, "Fetched restaurants from API: ${restaurants.size} entries")

            restaurantsDao.deleteAll()
            Log.d(TAG, "Deleted all local restaurant entries")

            restaurantsDao.insertAll(restaurants)
            Log.d(TAG, "Inserted fetched restaurant entries into local storage")

            versionsRepository.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            restaurants
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localRestaurants = restaurantsDao.getAll()
            Log.d(TAG, "Fetched restaurants from local storage: ${localRestaurants.size} entries")
            localRestaurants
        }
    }
}