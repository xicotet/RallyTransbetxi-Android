package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.repositories.ResultsRepository

class GetGlobalResultsUseCase(
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(): List<Result> {
        return resultsRepository.getGlobalResults()
    }
}