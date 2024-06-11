package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Warning
import com.canolabs.rallytransbetxi.data.sources.remote.WarningsServiceImpl
import javax.inject.Inject

interface WarningsRepository {
    suspend fun getWarnings(): List<Warning>
}

class WarningsRepositoryImpl @Inject constructor(
    private val warningsServiceImpl: WarningsServiceImpl
) : WarningsRepository {
    override suspend fun getWarnings(): List<Warning> {
        return warningsServiceImpl.fetchWarnings()
    }
}