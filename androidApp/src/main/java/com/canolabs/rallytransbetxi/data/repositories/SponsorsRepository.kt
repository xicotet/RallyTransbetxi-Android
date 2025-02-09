package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.sources.remote.SponsorsService

interface SponsorsRepository {
    suspend fun getNumberOfSponsors(): Int
}

class SponsorsRepositoryImpl(
    private val sponsorsService: SponsorsService
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