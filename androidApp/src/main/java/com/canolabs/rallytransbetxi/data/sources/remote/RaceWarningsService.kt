package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.RaceWarning
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RaceWarningsService {
    suspend fun fetchGlobalRaceWarning(): RaceWarning?
    suspend fun fetchStageRaceWarning(stageId: String): RaceWarning?
}

class RaceWarningsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : RaceWarningsService {
    override suspend fun fetchStageRaceWarning(stageId: String): RaceWarning? {
        return try {
            val warnings = mutableListOf<RaceWarning>()
            val querySnapshot =
                firebaseFirestore.collection("warnings/stage/$stageId").get().await()
            for (document in querySnapshot.documents) {
                val warning = document.toObject(RaceWarning::class.java)
                warning?.let {
                    warnings.add(it)
                }
            }
            warnings.last().copy(stageId = stageId) // We only want the last warning as only one warning can be displayed
        } catch (e: Exception) {
            Log.e("WarningsServiceImpl", "Error fetching stage warnings: ${e.message}")
            null
        }
    }

    override suspend fun fetchGlobalRaceWarning(): RaceWarning? {
        return try {
            val querySnapshot =
                firebaseFirestore.document("warnings/global").get().await()
            val warning = querySnapshot.toObject(RaceWarning::class.java)
            warning
        } catch (e: Exception) {
            Log.e("WarningsServiceImpl", "Error fetching global warnings: ${e.message}")
            null
        }
    }
}