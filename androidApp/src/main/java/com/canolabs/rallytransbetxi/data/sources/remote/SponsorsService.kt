package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface SponsorsService {
    suspend fun fetchNumberOfSponsors(): Int
}

class SponsorsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : SponsorsService {

    override suspend fun fetchNumberOfSponsors(): Int {
        return try {
            val documentSnapshot = firebaseFirestore.collection("sponsors")
                .document("quantity")
                .get()
                .await()

            // Extract the "total" field as an integer
            documentSnapshot.getLong("total")?.toInt() ?: 0
        } catch (e: Exception) {
            Log.e("SponsorsServiceImpl", "Error fetching number of sponsors: ${e.message}")
            0 // Return 0 in case of any errors
        }
    }
}