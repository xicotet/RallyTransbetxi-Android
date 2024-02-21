package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface StagesService {
    suspend fun fetchStages(): List<Stage>
}

class StagesServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : StagesService {
    override suspend fun fetchStages(): List<Stage> {
        return try {
            val stagesList = mutableListOf<Stage>()
            val querySnapshot = firebaseFirestore.collection("stages").get().await()
            for (document in querySnapshot.documents) {
                val stage = document.toObject(Stage::class.java)
                    ?.copy(acronym = document.id)
                stage?.let {
                    stagesList.add(it)
                }
            }
            stagesList
        } catch (e: Exception) {
            // Handle error
            emptyList()
        }
    }
}