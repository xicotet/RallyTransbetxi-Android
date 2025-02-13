package com.canolabs.rallytransbetxi.data.repositories

import android.content.Context
import com.canolabs.rallytransbetxi.utils.Constants.Companion.MIN_VERSION_KEY
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig
import dev.gitlive.firebase.remoteconfig.get

interface AppVersionRepository {
    suspend fun getCurrentVersion(): List<Int>
    suspend fun getMinAllowedVersion(): List<Int>
}

class AppVersionRepositoryImpl(
    private val context: Context,
    private val firebaseConfig: FirebaseRemoteConfig
) : AppVersionRepository {
    override suspend fun getCurrentVersion(): List<Int> {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = packageInfo.versionName?.split(".")?.map { it.toInt() } ?: listOf(0, 0, 0)
            if (version.size < 3) {
                version + List(3 - version.size) { 0 }
            } else {
                version
            }
        } catch (e: Exception) {
            println("AppVersionRepositoryImpl. Error getting current version: ${e.message}")
            listOf(0, 0, 0)
        }
    }

    override suspend fun getMinAllowedVersion(): List<Int> {
        return try {
            firebaseConfig.fetchAndActivate()
            val minVersion: String = firebaseConfig[MIN_VERSION_KEY]
            if (minVersion.isNotEmpty()) {
                minVersion.split(".").map { it.toInt() }
            } else {
                println("AppVersionRepositoryImpl. Error getting min version: $minVersion")
                listOf(0, 0, 0)
            }
        } catch (e: Exception) {
            println("AppVersionRepositoryImpl. Error getting min version: ${e.message}")
            listOf(0, 0, 0)
        }
    }
}