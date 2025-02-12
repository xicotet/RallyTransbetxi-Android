package com.canolabs.rallytransbetxi.data.sources.remote

import dev.gitlive.firebase.firestore.FirebaseFirestore
import com.canolabs.rallytransbetxi.data.models.responses.Stage

interface StagesService {
    suspend fun fetchStages(): List<Stage>
    suspend fun fetchStage(acronym: String): Stage?
}

class StagesServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : StagesService {

    override suspend fun fetchStages(): List<Stage> {
        return try {
            firebaseFirestore.collection("stages")
                .get()
                .documents
                .map { document ->
                    document.data<Stage>().copy(acronym = document.id)
                }
        } catch (e: Exception) {
            println("Error fetching stages: ${e.message}")
            emptyList()
        }
    }

    override suspend fun fetchStage(acronym: String): Stage? {
        return try {
            firebaseFirestore.collection("stages")
                .document(acronym)
                .get()
                .data<Stage>()
                .copy(acronym = acronym)
        } catch (e: Exception) {
            println("Error fetching stage $acronym: ${e.message}")
            null
        }
    }
}