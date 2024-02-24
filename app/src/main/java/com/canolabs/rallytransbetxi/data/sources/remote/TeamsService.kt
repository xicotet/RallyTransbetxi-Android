package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TeamsService {
    suspend fun fetchTeams(): List<Team>
}

class TeamsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : TeamsService {
    override suspend fun fetchTeams(): List<Team> {
        return try {
            val teamsList = mutableListOf<Team>()
            val querySnapshot = firebaseFirestore.collection("teams").get().await()
            for (document in querySnapshot.documents) {
                val team = document.toObject(Team::class.java)
                team?.let {
                    teamsList.add(it)
                }
            }
            teamsList
        } catch (e: Exception) {
            // Handle error
            emptyList()
        }
    }
}