package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.RaceWarning
import dev.gitlive.firebase.firestore.FirebaseFirestore

interface RaceWarningsService {
    suspend fun fetchGlobalRaceWarning(): RaceWarning?
    suspend fun fetchStageRaceWarning(stageId: String): RaceWarning?
}

class RaceWarningsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : RaceWarningsService {

    override suspend fun fetchStageRaceWarning(stageId: String): RaceWarning? {
        return try {
            firebaseFirestore.collection("warnings/stage/$stageId").get()
                .documents.map { it.data<RaceWarning>() }
                .lastOrNull() // We only want the last warning
                ?.copy(stageId = stageId)
        } catch (e: Exception) {
            println("Error fetching stage warnings: ${e.message}")
            null
        }
    }

    override suspend fun fetchGlobalRaceWarning(): RaceWarning? {
        return try {
            firebaseFirestore.collection("warnings/global").get()
                .documents.map { it.data<RaceWarning>() }
                .lastOrNull() // Ensure we only get the latest global warning
        } catch (e: Exception) {
            println("Error fetching global warnings: ${e.message}")
            null
        }
    }
}