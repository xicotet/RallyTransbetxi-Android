package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Statement
import com.canolabs.rallytransbetxi.data.sources.local.dao.StatementDao
import com.canolabs.rallytransbetxi.data.sources.remote.StatementsService

interface StatementsRepository {
    suspend fun getStatements(): List<Statement>
}

class StatementsRepositoryImpl(
    private val statementsService: StatementsService,
    private val versionsRepository: VersionsRepository,
    private val statementsDao: StatementDao,
) : StatementsRepository {

    companion object {
        private const val TAG = "StatementsRepositoryImpl"
    }

    override suspend fun getStatements(): List<Statement> {
        val versionName = "statements"
        Log.d(TAG, "getStatements() called")

        val localVersionCount = versionsRepository.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found")
            val apiVersion = versionsRepository.getApiVersion(versionName) ?: return emptyList()

            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val statements = statementsService.fetchStatements()
            Log.d(TAG, "Fetched statements from API: ${statements.size} statements")

            // The latest non-seen statemeent needs to be prompted as a dialog
            val shortedStatementsByDate = statements.sortedByDescending { it.date }
            if (shortedStatementsByDate.isNotEmpty()) {
                shortedStatementsByDate.first().needsToBePromptedAsDialog = true
            }

            statementsDao.insertStatements(shortedStatementsByDate)
            Log.d(TAG, "Inserted fetched statements into local storage")

            return shortedStatementsByDate
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
            val statements = statementsService.fetchStatements()
            Log.d(TAG, "Fetched statements from API: ${statements.size} statements")

            // The latest non-seen statement needs to be prompted as a dialog
            val shortedStatementsByDate = statements.sortedByDescending { it.date }
            if (shortedStatementsByDate.isNotEmpty()) {
                shortedStatementsByDate.first().needsToBePromptedAsDialog = true
            }

            statementsDao.deleteAll()
            Log.d(TAG, "Deleted all local statements")

            statementsDao.insertStatements(shortedStatementsByDate)
            Log.d(TAG, "Inserted fetched statements into local storage")

            versionsRepository.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            shortedStatementsByDate
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localStatements = statementsDao.getStatements()
            Log.d(TAG, "Fetched statements from local storage: ${localStatements.size} statements")

            // No new statements, so no need to display the dialog
            localStatements.map { it.needsToBePromptedAsDialog = false }

            localStatements
        }
    }
}