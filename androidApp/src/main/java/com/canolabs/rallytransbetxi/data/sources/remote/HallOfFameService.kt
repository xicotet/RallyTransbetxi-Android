package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame
import dev.gitlive.firebase.firestore.FirebaseFirestore


interface HallOfFameService {
    suspend fun fetchHallOfFame(): List<HallOfFame>
}


class HallOfFameServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : HallOfFameService {
    override suspend fun fetchHallOfFame(): List<HallOfFame> {
        return try {
            val querySnapshot = firebaseFirestore.collection("hall_of_fame").get()
            querySnapshot.documents.map { document ->
                document.data<HallOfFame>()
            }
        } catch (e: Exception) {
            println("Error fetching hall of fame: ${e.message}")
            emptyList()
        }
    }
}