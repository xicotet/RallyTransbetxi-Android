package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.SponsorsRepository

class GetNumberOfSponsorsUseCase(
    private val sponsorsRepository: SponsorsRepository
) {
    suspend fun invoke(): Int {
        return sponsorsRepository.getNumberOfSponsors()
    }
}