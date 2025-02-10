package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.requests.PlaceSearchRequest
import com.canolabs.rallytransbetxi.data.models.responses.PlaceSearchResponse
import com.canolabs.rallytransbetxi.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class PlacesService {
    private val apiKey: String by inject(String::class.java, named(Constants.MAPS_API_KEY_QUALIFIER))
    private val client: HttpClient by inject(HttpClient::class.java, named(Constants.PLACES_CLIENT_QUALIFIER))

    suspend fun searchNearbyRestaurants(
        fieldMask: String,
        body: PlaceSearchRequest
    ): PlaceSearchResponse {
        val response: HttpResponse = client.post {
            url {
                appendPathSegments("places:searchNearby")
            }
            headers {
                append("X-Goog-Api-Key", apiKey)
                append("X-Goog-FieldMask", fieldMask)
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        return response.body()
    }
}