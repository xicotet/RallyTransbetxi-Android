package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface HallOfFameService {
    suspend fun fetchHallOfFame(): List<HallOfFame>
}

class HallOfFameServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
): HallOfFameService {
    override suspend fun fetchHallOfFame(): List<HallOfFame> {

        return try {
            val winners = mutableListOf<HallOfFame>()
            val querySnapshot = firebaseFirestore.collection("hall_of_fame").get().await()
            for (document in querySnapshot.documents) {
                val winner = document.toObject(HallOfFame::class.java)
                winner?.let {
                    winners.add(it)
                }
            }
            winners
        } catch (e: Exception) {
            Log.d("HallOfFameServiceImpl", "Error fetching winners: ${e.message}")
            emptyList()
        }
    }
}