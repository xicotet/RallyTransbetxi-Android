package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface ResultsService {
    suspend fun fetchGlobalResults(): List<Result>
}

class ResultsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val teamsServiceImpl: TeamsServiceImpl
) : ResultsService {

    override suspend fun fetchGlobalResults(): List<Result> {
        val startupTime = System.currentTimeMillis()

        val documentSnapshot = firebaseFirestore.collection("results/global/ranking").get().await()
        val results = coroutineScope {
            documentSnapshot.documents.map {
                async {
                    val result = it.toObject(Result::class.java)
                    Log.d("ResultsServiceImpl", "Result: $result")
                    val teamReference = it["teamReference"] as DocumentReference
                    Log.d("ResultsServiceImpl", "Team reference: $teamReference")
                    val team = teamsServiceImpl.fetchTeamByReference(teamReference)
                    result?.team = team
                    result
                }
            }.awaitAll().filterNotNull()
        }
        val endTime = System.currentTimeMillis()
        Log.d("ResultsServiceImpl", "Time: ${endTime - startupTime} ms")
        return results
    }
}