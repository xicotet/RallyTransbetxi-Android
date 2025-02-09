package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Activity
import com.canolabs.rallytransbetxi.data.repositories.ActivitiesRepository

class GetActivitiesUseCase(
    private val activitiesRepository: ActivitiesRepository
) {
    suspend operator fun invoke(): List<Activity> {
        return activitiesRepository.getActivities()
    }
}