package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.repositories.TeamsRepositoryImpl
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException

interface ResultsService {
    suspend fun fetchGlobalResults(): List<Result>
    suspend fun fetchStageResults(stageId: String): List<Result>
}

class ResultsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val teamsRepositoryImpl: TeamsRepositoryImpl // TODO: Refactor this to avoid injecting a repository...
) : ResultsService {

    override suspend fun fetchGlobalResults(): List<Result> {
        return fetchResults("results/global/ranking")
    }

    override suspend fun fetchStageResults(stageId: String): List<Result> {
        return fetchResults("results/stage/$stageId")
    }

    private suspend fun fetchResults(collectionPath: String): List<Result> {
        return try {
            val teams = teamsRepositoryImpl.getTeams().associateBy { it.number }
            val querySnapshot = firebaseFirestore.collection(collectionPath).get()

            querySnapshot.documents.mapNotNull { document ->
                try {
                    fetchResult(document, teams)
                } catch (e: Exception) {
                    println("Error processing result ${document.id}: ${e.message}")
                    null
                }
            }
        } catch (e: FirebaseFirestoreException) {
            println("Error fetching results from $collectionPath: ${e.message}")
            emptyList()
        }
    }

    private fun fetchResult(document: DocumentSnapshot, teams: Map<String, Team>): Result {
        val result = document.data<Result>()
        val teamId = document.get<DocumentReference>("teamReference")
        result.team = teams[teamId.id] ?: Team()
        return result
    }
}