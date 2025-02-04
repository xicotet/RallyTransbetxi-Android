package com.canolabs.rallytransbetxi.data.repositories

import android.content.Context
import android.util.Log
import com.canolabs.rallytransbetxi.utils.Constants.Companion.MIN_VERSION_KEY
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AppVersionRepository {
    suspend fun getCurrentVersion(): List<Int>
    suspend fun getMinAllowedVersion(): List<Int>
}

class AppVersionRepositoryImpl @Inject constructor(
    private val context: Context,
) : AppVersionRepository {
    override suspend fun getCurrentVersion(): List<Int> {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = packageInfo.versionName.split(".").map { it.toInt() }
            // If version doesn't have 3 parts, add 0s to the end
            if (version.size < 3) {
                version + List(3 - version.size) { 0 }
            } else {
                version
            }
        } catch (e: Exception) {
            Log.e("AppVersionRepositoryImpl", "Error fetching current version: ${e.message}")
            listOf(0, 0, 0)
        }
    }

    override suspend fun getMinAllowedVersion(): List<Int> {
        val remoteConfig = Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            })
        }

        return try {
            // Fetch and activate with explicit success and failure listeners for better logging
            remoteConfig.fetchAndActivate().addOnSuccessListener {
                Log.d("AppVersionRepositoryImpl", "Remote config fetched and activated")
            }.addOnFailureListener { e ->
                Log.e("AppVersionRepositoryImpl", "Error fetching remote config: ${e.message}", e)
            }.await()

            // Get the min version as usual
            val minVersion = remoteConfig.getString(MIN_VERSION_KEY)
            if (minVersion.isNotEmpty()) {
                minVersion.split(".").map { it.toInt() }
            } else {
                Log.e("AppVersionRepositoryImpl", "MIN_VERSION_KEY not found in remote config.")
                listOf(0, 0, 0)
            }
        } catch (e: Exception) {
            Log.e("AppVersionRepositoryImpl", "Error fetching remote config: ${e.message}", e)
            listOf(0, 0, 0)
        }
    }
}