package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Activity
import com.canolabs.rallytransbetxi.data.repositories.ActivitiesRepositoryImpl
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val activitiesRepositoryImpl: ActivitiesRepositoryImpl
) {
    suspend operator fun invoke(): List<Activity> {
        return activitiesRepositoryImpl.getActivities()
    }
}