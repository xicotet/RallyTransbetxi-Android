package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.sources.local.dao.TeamDao
import com.canolabs.rallytransbetxi.data.sources.remote.TeamsService
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkChecker

interface TeamsRepository {
    suspend fun getTeams(): List<Team>
}

class TeamsRepositoryImpl(
    private val teamsService: TeamsService,
    private val teamDao: TeamDao,
    private val versionsRepository: VersionsRepository,
    private val networkChecker: NetworkChecker
) : TeamsRepository {

    companion object {
        private const val TAG = "TeamsRepositoryImpl"
    }

    override suspend fun getTeams(): List<Team> {
        val versionName = "teams"
        Log.d(TAG, "getTeams() called")

        val localVersionCount = versionsRepository.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no stable network connection, fetch from local storage
        if (!networkChecker.isNetworkAvailable()) {
            Log.w(TAG, "Network is unavailable. Fetching data from local storage.")
            val localTeams = teamDao.getTeams()
            Log.d(TAG, "Fetched teams from local storage: ${localTeams.size} teams")
            return localTeams
        } else {
            Log.d(TAG, "Network is available.")
        }

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found")
            val apiVersion = versionsRepository.getApiVersion(versionName) ?: return emptyList()
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val teams = teamsService.fetchTeams()
            Log.d(TAG, "Fetched teams from API: ${teams.size} teams")

            teamDao.insertTeams(teams)
            Log.d(TAG, "Inserted fetched teams into local storage")

            return teams
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
            val teams = teamsService.fetchTeams()
            Log.d(TAG, "Fetched teams from API: ${teams.size} teams")

            teamDao.deleteAllTeams()
            Log.d(TAG, "Deleted all local teams")

            teamDao.insertTeams(teams)
            Log.d(TAG, "Inserted fetched teams into local storage")

            versionsRepository.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted local version for '$versionName'")

            versionsRepository.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            teams
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localTeams = teamDao.getTeams()
            Log.d(TAG, "Fetched teams from local storage: ${localTeams.size} teams")
            localTeams
        }
    }
}