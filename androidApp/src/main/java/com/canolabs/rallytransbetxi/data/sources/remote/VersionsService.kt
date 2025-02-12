package com.canolabs.rallytransbetxi.data.sources.remote

import dev.gitlive.firebase.firestore.FirebaseFirestore
import com.canolabs.rallytransbetxi.data.models.responses.Version

interface VersionsService {
    suspend fun fetchVersion(name: String): Version?
}

class VersionsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : VersionsService {

    override suspend fun fetchVersion(name: String): Version? {
        return try {
            val documentSnapshot = firebaseFirestore.collection("versions")
                .document(name)
                .get()

            documentSnapshot.data<Version>().copy(name = name)
        } catch (e: Exception) {
            println("Error fetching version: ${e.message}")
            null
        }
    }
}