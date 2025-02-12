package com.canolabs.rallytransbetxi.data.sources.remote

import dev.gitlive.firebase.firestore.FirebaseFirestore
import com.canolabs.rallytransbetxi.data.models.responses.Statement

interface StatementsService {
    suspend fun fetchStatements(): List<Statement>
}

class StatementsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : StatementsService {

    override suspend fun fetchStatements(): List<Statement> {
        return try {
            firebaseFirestore.collection("statements")
                .get()
                .documents
                .map { document -> document.data<Statement>() }
        } catch (e: Exception) {
            println("Error fetching statements: ${e.message}")
            emptyList()
        }
    }
}