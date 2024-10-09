package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Warning
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface WarningsService {
    suspend fun fetchWarnings(): List<Warning>
}

class WarningsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : WarningsService {
    override suspend fun fetchWarnings(): List<Warning> {
        return try {
            val warnings = mutableListOf<Warning>()
            val querySnapshot = firebaseFirestore.collection("warnings").get().await()
            for (document in querySnapshot.documents) {
                val warning = document.toObject(Warning::class.java)
                warning?.let {
                    warnings.add(it)
                }
            }
            warnings
        } catch (e: Exception) {
            Log.d("WarningsServiceImpl", "Error fetching warnings: ${e.message}")
            emptyList()
        }
    }
}