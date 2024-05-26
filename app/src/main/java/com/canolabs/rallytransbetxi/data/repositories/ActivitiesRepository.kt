package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Activity
import com.canolabs.rallytransbetxi.data.sources.remote.ActivitiesServiceImpl
import javax.inject.Inject

interface ActivitiesRepository {
    suspend fun getActivities(): List<Activity>
}

class ActivitiesRepositoryImpl @Inject constructor(
    private val activitiesServiceImpl: ActivitiesServiceImpl
) : ActivitiesRepository {
    override suspend fun getActivities(): List<Activity> {
        return activitiesServiceImpl.fetchActivities()
    }
}