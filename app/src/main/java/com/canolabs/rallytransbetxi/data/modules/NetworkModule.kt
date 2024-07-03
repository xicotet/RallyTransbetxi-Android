package com.canolabs.rallytransbetxi.data.modules

import android.content.Context
import com.canolabs.rallytransbetxi.data.sources.remote.DirectionsService
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

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // Create a logging interceptor
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        // Create an OkHttpClient and add the logging interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.DIRECTIONS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Set the client with the interceptor
            .build()
    }

    @Provides
    fun provideDirectionsService(retrofit: Retrofit): DirectionsService {
        return retrofit.create(DirectionsService::class.java)
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