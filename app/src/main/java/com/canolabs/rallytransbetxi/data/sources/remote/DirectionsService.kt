package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Directions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsService {
    @GET("directions/driving-car")
    fun getDirections(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Call<Directions>
}