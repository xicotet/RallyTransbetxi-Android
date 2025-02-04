package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.News
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface NewsService {
    suspend fun fetchNews(): List<News>
}

class NewsServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : NewsService {
    override suspend fun fetchNews(): List<News> {
        return try {
            val newsList = mutableListOf<News>()
            val querySnapshot = firebaseFirestore.collection("news").get().await()
            for (document in querySnapshot.documents) {
                val news = document.toObject(News::class.java)
                news?.let {
                    newsList.add(it)
                }
            }
            newsList
        } catch (e: Exception) {
            Log.e("NewsServiceImpl", "Error fetching news: ${e.message}")
            emptyList()
        }
    }
}