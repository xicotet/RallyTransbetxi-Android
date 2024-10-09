package com.canolabs.rallytransbetxi.domain.usecases

import android.util.Log
import com.canolabs.rallytransbetxi.data.repositories.ResultsRepositoryImpl
import javax.inject.Inject
import com.canolabs.rallytransbetxi.data.models.responses.Result

class GetStagesResultsUseCase @Inject constructor(
    private val resultsRepositoryImpl: ResultsRepositoryImpl
) {
    suspend operator fun invoke(stageId: String): List<Result> {
        Log.d("GetStagesResultsUseCase", "stageId: $stageId")
        return resultsRepositoryImpl.getStageResults(stageId)
    }
}