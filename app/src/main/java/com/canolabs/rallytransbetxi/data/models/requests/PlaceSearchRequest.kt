package com.canolabs.rallytransbetxi.data.models.requests

import com.canolabs.rallytransbetxi.data.models.responses.LatLng

data class PlaceSearchRequest(
    val includedTypes: List<String>? = null,
    val excludedTypes: List<String>? = null,
    val includedPrimaryTypes: List<String>? = null,
    val excludedPrimaryTypes: List<String>? = null,
    val rankPreference: String? = null,
    val languageCode: String? = "en", // Language code for results, default to "en"
    val locationRestriction: LocationRestriction? = null
)

data class Circle(
    val center: LatLng,
    val radius: Double // Radius in meters
)

data class LocationRestriction(
    val circle: Circle
)