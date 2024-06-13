package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.canolabs.rallytransbetxi.data.sources.local.dao.CategoryDao
import com.canolabs.rallytransbetxi.data.sources.remote.CategoriesServiceImpl
import javax.inject.Inject


interface CategoriesRepository {
    suspend fun getCategories(): List<Category>
}

class CategoriesRepositoryImpl @Inject constructor(
    private val categoriesServiceImpl: CategoriesServiceImpl,
    private val versionsRepositoryImpl: VersionsRepositoryImpl,
    private val categoriesDao: CategoryDao
) : CategoriesRepository {

    companion object {
        private const val TAG = "CategoriesRepositoryImpl"
    }

    override suspend fun getCategories(): List<Category> {
        val versionName = "categories"
        Log.d(TAG, "getCategories() called")

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found. Fetching from API.")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val categories = categoriesServiceImpl.fetchCategories()
            Log.d(TAG, "Fetched categories from API: ${categories.size} categories")

            categoriesDao.insertCategories(categories)
            Log.d(TAG, "Inserted fetched categories into local storage")

            return categories
        }

        // Get local and API versions
        val localVersion = versionsRepositoryImpl.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        // Compare versions
        return if (apiVersion != localVersion) {
            Log.d(TAG, "API version is different. Fetching data from API.")

            // If API version is newer, fetch from API and update local version
            categoriesDao.deleteAllCategories()
            Log.d(TAG, "Deleted all local categories")

            val categories = categoriesServiceImpl.fetchCategories()
            Log.d(TAG, "Fetched categories from API: ${categories.size} categories")

            categoriesDao.insertCategories(categories)
            Log.d(TAG, "Inserted fetched categories into local storage")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            categories
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val categories = categoriesDao.getCategories()
            Log.d(TAG, "Fetched categories from local storage: ${categories.size} categories")
            categories
        }
    }
}