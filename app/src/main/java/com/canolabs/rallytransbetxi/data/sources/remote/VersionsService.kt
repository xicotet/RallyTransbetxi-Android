package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Version
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface VersionsService {
    suspend fun fetchVersion(name: String): Version?
}

class VersionsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : VersionsService {
    override suspend fun fetchVersion(name: String): Version? {
        return try {
            val documentSnapshot = firebaseFirestore.collection("versions").document(name).get().await()
            val version = documentSnapshot.toObject(Version::class.java)
            version?.let {
                it.name = documentSnapshot.id
            }
            version
        } catch (e: Exception) {
            Log.d("VersionsServiceImpl", "Error fetching version: ${e.message}")
            null
        }
    }
}