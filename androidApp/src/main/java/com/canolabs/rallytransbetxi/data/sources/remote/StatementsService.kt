package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Statement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


interface StatementsService {
    suspend fun fetchStatements(): List<Statement>
}

class StatementsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : StatementsService {
    override suspend fun fetchStatements(): List<Statement> {
        return try {
            val statements = mutableListOf<Statement>()
            val querySnapshot = firebaseFirestore.collection("statements").get().await()
            for (document in querySnapshot.documents) {
                val statement = document.toObject(Statement::class.java)
                statement?.let {
                    statements.add(it)
                }
            }
            statements
        } catch (e: Exception) {
            Log.e("WarningsServiceImpl", "Error fetching warnings: ${e.message}")
            emptyList()
        }
    }
}