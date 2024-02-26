package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.repositories.ResultsRepositoryImpl
import javax.inject.Inject

class GetGlobalResultsUseCase @Inject constructor(
    private val resultsRepositoryImpl: ResultsRepositoryImpl
) {
    suspend operator fun invoke(): List<Result> {
        return resultsRepositoryImpl.getGlobalResults()
    }
}