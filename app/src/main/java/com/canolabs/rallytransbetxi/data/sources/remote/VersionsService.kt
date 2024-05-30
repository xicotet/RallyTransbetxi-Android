package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Version
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface VersionsService {
    suspend fun fetchVersions(): List<Version>
}

class VersionsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : VersionsService {
    override suspend fun fetchVersions(): List<Version> {
        return try {
            val versions = mutableListOf<Version>()
            val querySnapshot = firebaseFirestore.collection("versions").get().await()
            for (document in querySnapshot.documents) {
                val version = document.toObject(Version::class.java)
                version?.let {
                    version.name = document.id
                    versions.add(it)
                }
            }
            versions
        } catch (e: Exception) {
            Log.d("ActivitiesServiceImpl", "Error fetching activities: ${e.message}")
            emptyList()
        }
    }
}