package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.data.sources.local.dao.NewsDao
import com.canolabs.rallytransbetxi.data.sources.remote.NewsServiceImpl
import javax.inject.Inject

interface NewsRepository {
    suspend fun getNews(): List<News>
}

class NewsRepositoryImpl @Inject constructor(
    private val newsServiceImpl: NewsServiceImpl,
    private val versionsRepositoryImpl: VersionsRepositoryImpl,
    private val newsDao: NewsDao
) : NewsRepository {

    companion object {
        private const val TAG = "NewsRepositoryImpl"
    }

    override suspend fun getNews(): List<News> {
        val versionName = "news"
        Log.d(TAG, "getNews() called")

        val localVersionCount = versionsRepositoryImpl.countLocalStoredVersionsByName(versionName)
        Log.d(TAG, "Local version count for '$versionName': $localVersionCount")

        // If there is no local version stored, fetch from API and store the version
        if (localVersionCount == 0) {
            Log.d(TAG, "No local version found. Fetching from API.")
            val apiVersion = versionsRepositoryImpl.getApiVersion(versionName) ?: return emptyList()
            Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Inserted API version into local storage: $apiVersion")

            val news = newsServiceImpl.fetchNews()
            Log.d(TAG, "Fetched news from API: ${news.size} news")

            newsDao.insertNews(news)
            Log.d(TAG, "Inserted fetched news into local storage")

            return news
        }

        // Get local and API versions
        val localVersion = versionsRepositoryImpl.getLocalStoredVersion(versionName)
        Log.d(TAG, "Fetched local version for '$versionName': $localVersion")

        val apiVersion = versionsRepositoryImpl.getApiVersion(versionName)
        Log.d(TAG, "Fetched API version for '$versionName': $apiVersion")

        // Compare versions
        return if (apiVersion != null && apiVersion != localVersion) {
            Log.d(TAG, "API version is different. Fetching data from API.")

            // If API version is newer, fetch from API and update local version
            val news = newsServiceImpl.fetchNews()
            Log.d(TAG, "Fetched news from API: ${news.size} news")

            newsDao.deleteAll()
            Log.d(TAG, "Deleted all local news")

            newsDao.insertNews(news)
            Log.d(TAG, "Inserted fetched news into local storage")

            versionsRepositoryImpl.deleteLocalStoredVersion(versionName)
            Log.d(TAG, "Deleted old local version")

            versionsRepositoryImpl.insertLocalStoredVersion(versionName, apiVersion)
            Log.d(TAG, "Add local version to API version: $apiVersion")

            news
        } else {
            Log.d(TAG, "Local version is up-to-date. Fetching data from local storage.")
            // If local version is up-to-date, return data from local storage
            val localNews = newsDao.getAll()
            Log.d(TAG, "Fetched news from local storage: ${localNews.size} news")
            localNews
        }
    }
}