package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.RaceWarning
import com.canolabs.rallytransbetxi.data.sources.local.dao.RaceWarningDao
import com.canolabs.rallytransbetxi.data.sources.remote.RaceWarningsService

interface RaceWarningsRepository {
    suspend fun getGlobalRaceWarning(): RaceWarning?
    suspend fun getStageRaceWarning(stageId: String): RaceWarning?
}

class RaceWarningsRepositoryImpl(
    private val raceWarningsService: RaceWarningsService,
    private val raceWarningDao: RaceWarningDao
) : RaceWarningsRepository {

    override suspend fun getGlobalRaceWarning(): RaceWarning? {
        Log.d("RaceWarningsRepo", "Fetching global race warning...")

        val apiFetchedGlobalWarning = raceWarningsService.fetchGlobalRaceWarning()
        Log.d("RaceWarningsRepo", "API fetched global warning: $apiFetchedGlobalWarning")

        val localStoredGlobalWarning = raceWarningDao.getGlobalRaceWarnings().firstOrNull()
        Log.d("RaceWarningsRepo", "Local stored global warning: $localStoredGlobalWarning")

        return when {
            // Case 1: No API warning available
            apiFetchedGlobalWarning == null -> {
                Log.d("RaceWarningsRepo", "No API warning available. Deleting local warnings...")
                raceWarningDao.deleteGlobalRaceWarnings()
                null
            }

            // Case 2: API warning matches the local warning
            apiFetchedGlobalWarning == localStoredGlobalWarning -> {
                Log.d("RaceWarningsRepo", "API warning matches local warning.")
                if (localStoredGlobalWarning.alwaysShow) {
                    Log.d("RaceWarningsRepo", "Returning API warning (alwaysShow is true).")
                    apiFetchedGlobalWarning
                } else {
                    Log.d("RaceWarningsRepo", "Not showing API warning (alwaysShow is false).")
                    null
                }
            }

            // Case 3: API warning is new or no local warning exists
            else -> {
                Log.d("RaceWarningsRepo", "API warning is new or no local warning exists. Updating local data...")
                raceWarningDao.deleteGlobalRaceWarnings()
                raceWarningDao.insertRaceWarning(apiFetchedGlobalWarning)
                Log.d("RaceWarningsRepo", "Inserted new API warning into local storage.")
                apiFetchedGlobalWarning
            }
        }
    }

    override suspend fun getStageRaceWarning(stageId: String): RaceWarning? {
        Log.d("RaceWarningsRepo", "Fetching stage race warning for stage: $stageId...")

        val apiFetchedStageWarning = raceWarningsService.fetchStageRaceWarning(stageId)
        Log.d("RaceWarningsRepo", "API fetched stage warning: $apiFetchedStageWarning")

        val localStoredStageWarning = raceWarningDao.getStageRaceWarnings(stageId).firstOrNull()
        Log.d("RaceWarningsRepo", "Local stored stage warning for stage $stageId: $localStoredStageWarning")

        return when {
            // Case 1: No API warning available
            apiFetchedStageWarning == null -> {
                Log.d("RaceWarningsRepo", "No API warning available for stage $stageId. Deleting local warnings...")
                raceWarningDao.deleteStageRaceWarnings(stageId)
                null
            }

            // Case 2: API warning matches the local warning
            apiFetchedStageWarning == localStoredStageWarning -> {
                Log.d("RaceWarningsRepo", "API warning matches local warning for stage $stageId.")
                if (localStoredStageWarning.alwaysShow) {
                    Log.d("RaceWarningsRepo", "Returning API warning (alwaysShow is true) for stage $stageId.")
                    apiFetchedStageWarning
                } else {
                    Log.d("RaceWarningsRepo", "Not showing API warning (alwaysShow is false) for stage $stageId.")
                    null
                }
            }

            // Case 3: API warning is new or no local warning exists
            else -> {
                Log.d("RaceWarningsRepo", "API warning is new or no local warning exists for stage $stageId. Updating local data...")
                raceWarningDao.deleteStageRaceWarnings(stageId)
                raceWarningDao.insertRaceWarning(apiFetchedStageWarning)
                Log.d("RaceWarningsRepo", "Inserted new API warning into local storage for stage $stageId.")
                apiFetchedStageWarning
            }
        }
    }
}