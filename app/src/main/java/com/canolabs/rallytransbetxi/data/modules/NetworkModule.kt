package com.canolabs.rallytransbetxi.data.modules

import android.content.Context
import com.canolabs.rallytransbetxi.data.sources.remote.DirectionsService
import com.canolabs.rallytransbetxi.data.sources.remote.PlacesService
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkChecker
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkCheckerImpl
import com.canolabs.rallytransbetxi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Provide Retrofit for Directions API
    @Provides
    @Singleton
    @DirectionsRetrofit
    fun provideDirectionsRetrofit(): Retrofit {
        val baseUrl = Constants.DIRECTIONS_BASE_URL
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Provide Retrofit for Places API
    @Provides
    @Singleton
    @PlacesRetrofit
    fun providePlacesRetrofit(): Retrofit {
        val baseUrl = Constants.PLACES_BASE_URL
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Provide the DirectionsService (using the DirectionsRetrofit instance)
    @Provides
    @Singleton
    fun provideDirectionsService(@DirectionsRetrofit retrofit: Retrofit): DirectionsService {
        return retrofit.create(DirectionsService::class.java)
    }

    // Provide the PlacesService (using the PlacesRetrofit instance)
    @Provides
    @Singleton
    fun providePlacesService(@PlacesRetrofit retrofit: Retrofit): PlacesService {
        return retrofit.create(PlacesService::class.java)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkCheckerImpl(context)
    }
}