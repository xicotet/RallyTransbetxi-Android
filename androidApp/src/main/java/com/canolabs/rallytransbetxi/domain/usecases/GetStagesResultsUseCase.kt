package com.canolabs.rallytransbetxi.domain.usecases

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.repositories.ResultsRepository

class GetStagesResultsUseCase(
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(stageId: String): List<Result> {
        Log.d("GetStagesResultsUseCase", "stageId: $stageId")
        return resultsRepository.getStageResults(stageId)
    }
}