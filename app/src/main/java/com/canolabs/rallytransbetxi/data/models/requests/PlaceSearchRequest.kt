package com.canolabs.rallytransbetxi.data.models.requests

import com.canolabs.rallytransbetxi.data.models.responses.LatLng

data class PlaceSearchRequest(
    val textQuery: String,
    val languageCode: String? = null,  // Optional
    // val minRating: Double = 1.0,  // TODO: Remove these optional parameters
    val locationRestriction: LocationRestriction? = null  // TODO: Remove these optional parameters
)

data class Rectangle(
    val low: LatLng,
    val high: LatLng
)

data class LocationRestriction(
    val rectangle: Rectangle
)