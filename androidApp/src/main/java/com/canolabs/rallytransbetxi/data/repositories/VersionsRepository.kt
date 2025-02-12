package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Version
import com.canolabs.rallytransbetxi.data.sources.local.dao.VersionsDao
import com.canolabs.rallytransbetxi.data.sources.remote.VersionsService
import dev.gitlive.firebase.firestore.Timestamp

interface VersionsRepository {
    suspend fun getLocalStoredVersion(name: String): Timestamp
    suspend fun countLocalStoredVersionsByName(name: String): Int
    suspend fun insertLocalStoredVersion(name: String, timestamp: Timestamp)
    suspend fun deleteLocalStoredVersion(name: String)
    suspend fun getApiVersion(name: String): Timestamp?
}

class VersionsRepositoryImpl(
    private val versionsDao: VersionsDao,
    private val versionsService: VersionsService
) : VersionsRepository {
    override suspend fun getLocalStoredVersion(name: String): Timestamp {
        return versionsDao.getVersion(name)
    }

    override suspend fun countLocalStoredVersionsByName(name: String): Int {
        return versionsDao.countVersionsByName(name)
    }

    override suspend fun insertLocalStoredVersion(name: String, timestamp: Timestamp) {
        versionsDao.insertVersion(Version(name = name, timestamp =  timestamp))
    }

    override suspend fun deleteLocalStoredVersion(name: String) {
        versionsDao.deleteVersion(name)
    }

    override suspend fun getApiVersion(name: String): Timestamp? {
        return versionsService.fetchVersion(name)?.timestamp
    }
}