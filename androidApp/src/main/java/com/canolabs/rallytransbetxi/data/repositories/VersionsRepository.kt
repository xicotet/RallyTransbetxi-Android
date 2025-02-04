package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Version
import com.canolabs.rallytransbetxi.data.sources.local.dao.VersionsDao
import com.canolabs.rallytransbetxi.data.sources.remote.VersionsServiceImpl
import com.google.firebase.Timestamp
import javax.inject.Inject

interface VersionsRepository {
    suspend fun getLocalStoredVersion(name: String): Timestamp
    suspend fun countLocalStoredVersionsByName(name: String): Int
    suspend fun insertLocalStoredVersion(name: String, timestamp: Timestamp)
    suspend fun deleteLocalStoredVersion(name: String)
    suspend fun getApiVersion(name: String): Timestamp?
}

class VersionsRepositoryImpl @Inject constructor(
    private val versionsDao: VersionsDao,
    private val versionsServiceImpl: VersionsServiceImpl
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
        return versionsServiceImpl.fetchVersion(name)?.timestamp
    }

}