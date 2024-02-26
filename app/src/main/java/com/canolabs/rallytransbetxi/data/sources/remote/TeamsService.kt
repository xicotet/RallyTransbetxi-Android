package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TeamsService {
    suspend fun fetchTeams(): List<Team>
    suspend fun fetchTeamByReference(teamReference: DocumentReference): Team
}

class TeamsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val categoriesServiceImpl: CategoriesServiceImpl
) : TeamsService {
    override suspend fun fetchTeams(): List<Team> {
        val startTime = System.currentTimeMillis()
        val querySnapshot = firebaseFirestore.collection("teams").get().await()
        val teams = coroutineScope {
            querySnapshot.documents.map { document ->
                async {
                    try {
                        fetchTeam(document)
                    } catch (e: Exception) {
                        Log.d("TeamsServiceImpl", "Error getting team ${document.id}: ", e)
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }
        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        Log.d("TeamsServiceImpl", "Elapsed time for fetchTeams request: $elapsedTime ms")
        return teams
    }

    private suspend fun fetchTeam(
        document: DocumentSnapshot,
    ): Team {
        val team = document.toObject(Team::class.java) ?: return Team()
        val category = categoriesServiceImpl.fetchCategoryFromReference(
            document.get("categoryReference") as DocumentReference
        ).await()
        team.category = category
        return team
    }

    override suspend fun fetchTeamByReference(teamReference: DocumentReference): Team {
        val documentSnapshot = teamReference.get().await()
        return if (documentSnapshot != null) {
            fetchTeam(documentSnapshot)
        } else Team()
    }
}