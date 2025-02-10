package com.canolabs.rallytransbetxi.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class PlaceSearchResponse(
    val places: List<PlaceResponse>? = null
)

@Serializable
data class PlaceResponse(
    val name: String? = null,
    val id: String? = null,
    val displayName: LocalizedText? = null,
    val nationalPhoneNumber: String? = null,
    val internationalPhoneNumber: String? = null,
    val formattedAddress: String? = null,
    val shortFormattedAddress: String? = null,
    val location: LatLng? = null,
    val rating: Double? = null,
    val googleMapsUri: String? = null,
    val websiteUri: String? = null,
    val photos: List<Photo>? = null,
    val currentOpeningHours: OpeningHours? = null,
    val googleMapsLinks: GoogleMapsLinks? = null,
    val priceRange: PriceRange? = null,
)

@Serializable
data class LocalizedText(
    val language: String? = null,
    val text: String? = null
)

@Serializable
data class LatLng(
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class OpeningHours(
    val openNow: Boolean? = null,
    val weekdayText: List<String>? = null
)

@Serializable
data class Photo(
    val name: String? = null,
    val widthPx: Int? = null,
    val heightPx: Int? = null,
)

@Serializable
data class GoogleMapsLinks(
    val url: String? = null
)

@Serializable
data class PriceRange(
    val startPrice: Price? = null,
    val endPrice: Price? = null
)

@Serializable
data class Price(
    val currencyCode: String? = null,
    val units: String? = null
)