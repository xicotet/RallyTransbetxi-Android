package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.RaceWarningsRepository

class GetStageRaceWarningUseCase(
    private val raceWarningsRepository: RaceWarningsRepository
) {
    suspend operator fun invoke(stageId: String) = raceWarningsRepository.getStageRaceWarning(stageId)
}