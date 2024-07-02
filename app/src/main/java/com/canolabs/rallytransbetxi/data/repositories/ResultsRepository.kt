package com.canolabs.rallytransbetxi.data.repositories
import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.sources.local.dao.ResultDao
import com.canolabs.rallytransbetxi.data.sources.remote.ResultsServiceImpl
import javax.inject.Inject

interface ResultsRepository {
    suspend fun getGlobalResults(): List<Result>
    suspend fun getStageResults(stageId: String): List<Result>
}

class ResultsRepositoryImpl @Inject constructor(
    private val resultsServiceImpl: ResultsServiceImpl,
    private val resultsDao: ResultDao,
    private val versionsRepositoryImpl: VersionsRepositoryImpl
): ResultsRepository {
    companion object {
        private const val TAG = "ResultsRepositoryImpl"
    }

    override suspend fun getGlobalResults(): List<Result> {
        val startTime = System.currentTimeMillis()
        val versionName = "global_results"
        Log.d(TAG, "getGlobalResults() called")

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found. Fetching from API.")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val results = resultsServiceImpl.fetchGlobalResults().map { it.copy(isGlobal = true) }
            Log.d(TAG, "Fetched global results from API: ${results.size} results")

            resultsDao.insertResults(results)
            Log.d(TAG, "Inserted fetched global results into local storage")
            val endTime = System.currentTimeMillis()
            Log.d(TAG, "Time fetching global results: ${endTime - startTime} ms")
            return results
        }

        val localVersion = versionsRepositoryImpl.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        val resultsCount = resultsDao.countGlobalResults()

        return if (apiVersion != localVersion || resultsCount == 0) {
            if (resultsCount == 0) {
                Log.w(TAG, "No local results found global results " +
                    "despite of having a local version stored.")
            } else {
                Log.d(TAG, "API version is different. Fetching data from API.")
            }

            val results = resultsServiceImpl.fetchGlobalResults().map { it.copy(isGlobal = true) }
            Log.d(TAG, "Fetched global results from API: ${results.size} results")

            resultsDao.deleteGlobalResults()
            Log.d(TAG, "Deleted all local global results")

            resultsDao.insertResults(results)
            Log.d(TAG, "Inserted fetched global results into local storage")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted local version for '$versionName'")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            val endTime = System.currentTimeMillis()
            Log.d(TAG, "Time fetching global results: ${endTime - startTime} ms")
            results
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            val localResults = resultsDao.getGlobalResults()
            Log.d(TAG, "Fetched global results from local storage: ${localResults.size} results")
            val endTime = System.currentTimeMillis()
            Log.d(TAG, "Time fetching global results: ${endTime - startTime} ms")
            localResults
        }
    }

    override suspend fun getStageResults(stageId: String): List<Result> {
        val versionName = "stage_results_$stageId"
        Log.d(TAG, "getStageResults() called for stageId: $stageId")

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found. Fetching from API.")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val results = resultsServiceImpl.fetchStageResults(stageId).map { it.copy(stageId = stageId) }
            Log.d(TAG, "Fetched stage results from API: ${results.size} results")

            resultsDao.insertResults(results)
            Log.d(TAG, "Inserted fetched stage results into local storage")

            return results
        }

        val localVersion = versionsRepositoryImpl.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        val resultsCount = resultsDao.countStageResults(stageId)

        return if (apiVersion != localVersion || resultsCount == 0) {
            if (resultsCount == 0) {
                Log.w(TAG, "No local results found for stageId: $stageId " +
                    "despite of having a local version stored.")
            } else {
                Log.d(TAG, "API version is different. Fetching data from API.")
            }

            val results = resultsServiceImpl.fetchStageResults(stageId).map { it.copy(stageId = stageId) }
            Log.d(TAG, "Fetched stage results from API: ${results.size} results")

            resultsDao.deleteStageResults(stageId)
            Log.d(TAG, "Deleted all local results for stageId: $stageId")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted local version for '$versionName'")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            results
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            val localResults = resultsDao.getStageResults(stageId)
            Log.d(TAG, "Fetched stage results from local storage: ${localResults.size} results")
            localResults
        }
    }
}