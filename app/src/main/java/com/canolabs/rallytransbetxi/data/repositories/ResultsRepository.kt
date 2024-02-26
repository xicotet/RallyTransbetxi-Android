package com.canolabs.rallytransbetxi.data.repositories
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.sources.remote.ResultsServiceImpl
import javax.inject.Inject

interface ResultsRepository {
    suspend fun getGlobalResults(): List<Result>
}

class ResultsRepositoryImpl @Inject constructor(
    private val resultsServiceImpl: ResultsServiceImpl
): ResultsRepository {
    override suspend fun getGlobalResults(): List<Result> {
        return resultsServiceImpl.fetchGlobalResults()
    }
}