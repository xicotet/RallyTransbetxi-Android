package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.repositories.TeamsRepositoryImpl
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

interface ResultsService {
    suspend fun fetchGlobalResults(): List<Result>
    suspend fun fetchStageResults(stageId: String): List<Result>
}

class ResultsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val teamsRepositoryImpl: TeamsRepositoryImpl
) : ResultsService {

    override suspend fun fetchGlobalResults(): List<Result> {
        val startupTime = System.currentTimeMillis()

        val teams = teamsRepositoryImpl.getTeams().associateBy { it.number }
        val globalResults = mutableListOf<Result>()

        try {
            val documentSnapshot = firebaseFirestore.collection("results/global/ranking").get().await()
            for (document in documentSnapshot.documents) {
                try {
                    val result = fetchResult(document, teams)
                    globalResults.add(result)
                } catch (e: Exception) {
                    Log.d("ResultsServiceImpl", "Error processing result ${document.id}: ", e)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("ResultsServiceImpl", "Error fetching global results: ${e.message}", e)
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                Log.e("ResultsServiceImpl", "Permission denied for global results.")
            }
        }

        val endTime = System.currentTimeMillis()
        Log.d("ResultsServiceImpl", "Time fetching global results: ${endTime - startupTime} ms")
        return globalResults
    }

    private fun fetchResult(
        document: DocumentSnapshot,
        teams: Map<String, Team>
    ): Result {
        val result = document.toObject(Result::class.java) ?: return Result()
        val teamReference = document.get("teamReference") as DocumentReference
        result.team = teams[teamReference.id]!!
        return result
    }

    override suspend fun fetchStageResults(stageId: String): List<Result> {
        val startupTime = System.currentTimeMillis()

        val teams = teamsRepositoryImpl.getTeams().associateBy { it.number }
        val stageResults = mutableListOf<Result>()

        try {
            val documentSnapshot = firebaseFirestore.collection("results/stage/$stageId").get().await()
            for (document in documentSnapshot.documents) {
                try {
                    val result = fetchResult(document, teams)
                    stageResults.add(result)
                } catch (e: Exception) {
                    Log.d("ResultsServiceImpl", "Error processing result ${document.id}: ", e)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("ResultsServiceImpl", "Error fetching stage results: ${e.message}", e)
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                Log.e("ResultsServiceImpl", "Permission denied for stage results.")
            }
        }

        val endTime = System.currentTimeMillis()
        Log.d("ResultsServiceImpl", "Time fetching stage results: ${endTime - startupTime} ms")
        return stageResults
    }
}
