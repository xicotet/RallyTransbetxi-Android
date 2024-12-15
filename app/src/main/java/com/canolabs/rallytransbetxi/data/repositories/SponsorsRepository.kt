package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.sources.remote.SponsorsServiceImpl
import javax.inject.Inject

interface SponsorsRepository {
    suspend fun getNumberOfSponsors(): Int
}

class SponsorsRepositoryImpl @Inject constructor(
    private val sponsorsService: SponsorsServiceImpl
) : SponsorsRepository {

    companion object {
        private const val TAG = "SponsorsRepositoryImpl"
    }

    override suspend fun getNumberOfSponsors(): Int {
        return try {
            val totalSponsors = sponsorsService.fetchNumberOfSponsors()
            Log.d(TAG, "Fetched number of sponsors: $totalSponsors")
            totalSponsors
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching number of sponsors: ${e.message}")
            0 // Return 0 if there is an exception
        }
    }
}