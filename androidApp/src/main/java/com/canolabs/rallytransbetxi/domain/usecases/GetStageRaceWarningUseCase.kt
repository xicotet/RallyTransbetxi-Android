package com.canolabs.rallytransbetxi.domain.usecases


import com.canolabs.rallytransbetxi.data.repositories.RaceWarningsRepositoryImpl
import javax.inject.Inject

class GetStageRaceWarningUseCase @Inject constructor(
    private val raceWarningsRepository: RaceWarningsRepositoryImpl
) {
    suspend operator fun invoke(stageId: String) = raceWarningsRepository.getStageRaceWarning(stageId)
}