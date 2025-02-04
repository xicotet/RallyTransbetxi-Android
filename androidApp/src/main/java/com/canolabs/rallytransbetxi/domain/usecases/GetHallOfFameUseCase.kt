package com.canolabs.rallytransbetxi.domain.usecases

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame
import com.canolabs.rallytransbetxi.data.repositories.HallOfFameRepositoryImpl
import javax.inject.Inject

class GetHallOfFameUseCase @Inject constructor(
    private val hallOfFameRepositoryImpl: HallOfFameRepositoryImpl
) {
    suspend operator fun invoke(): List<HallOfFame> {
        Log.d("HallOfFameServiceImpl", "UseCase getHallOfFame()")
        return hallOfFameRepositoryImpl.getHallOfFame()
    }
}