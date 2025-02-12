package com.canolabs.rallytransbetxi.data.sources.remote

import dev.gitlive.firebase.firestore.FirebaseFirestore


interface SponsorsService {
    suspend fun fetchNumberOfSponsors(): Int
}

class SponsorsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : SponsorsService {

    override suspend fun fetchNumberOfSponsors(): Int {
        return try {
            firebaseFirestore.collection("sponsors")
                .document("quantity")
                .get()
                .data<Map<String, Any>>()["total"]?.toString()?.toIntOrNull() ?: 0
        } catch (e: Exception) {
            println("Error fetching number of sponsors: ${e.message}")
            0 // Return 0 in case of any errors
        }
    }
}