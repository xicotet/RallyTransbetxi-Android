package com.canolabs.rallytransbetxi.data.modules

import com.canolabs.rallytransbetxi.data.sources.remote.DirectionsService
import com.canolabs.rallytransbetxi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.DIRECTIONS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideDirectionsService(retrofit: Retrofit): DirectionsService {
        return retrofit.create(DirectionsService::class.java)
    }
}