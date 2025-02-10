package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Directions
import com.canolabs.rallytransbetxi.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class DirectionsService {
    private val directionsApiKey: String by inject(String::class.java, named(Constants.DIRECTIONS_API_KEY_QUALIFIER))
    private val client: HttpClient by inject(HttpClient::class.java, named(Constants.DIRECTIONS_CLIENT_QUALIFIER))

    suspend fun getDirections(profile: String, start: String, end: String): Directions {
        val response = client.get {
            url {
                appendPathSegments("directions", profile)
                parameters.append("api_key", directionsApiKey)
                parameters.append("start", start)
                parameters.append("end", end)
            }
        }
        return response.body()
    }
}