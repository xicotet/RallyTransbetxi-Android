package com.canolabs.rallytransbetxi.domain.usecases

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame
import com.canolabs.rallytransbetxi.data.repositories.HallOfFameRepository

class GetHallOfFameUseCase(
    private val hallOfFameRepository: HallOfFameRepository
) {
    suspend operator fun invoke(): List<HallOfFame> {
        Log.d("HallOfFameServiceImpl", "UseCase getHallOfFame()")
        return hallOfFameRepository.getHallOfFame()
    }
}