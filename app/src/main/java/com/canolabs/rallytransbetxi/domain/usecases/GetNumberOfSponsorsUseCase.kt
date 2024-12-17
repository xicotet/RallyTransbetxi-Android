package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.SponsorsRepositoryImpl
import javax.inject.Inject

class GetNumberOfSponsorsUseCase @Inject constructor(
    private val sponsorsRepository: SponsorsRepositoryImpl
) {
    suspend fun invoke(): Int {
        return sponsorsRepository.getNumberOfSponsors()
    }
}