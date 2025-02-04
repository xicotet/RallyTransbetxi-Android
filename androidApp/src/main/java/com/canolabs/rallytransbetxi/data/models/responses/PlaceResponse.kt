package com.canolabs.rallytransbetxi.data.models.responses

data class PlaceSearchResponse(
    val places: List<PlaceResponse>
)

data class PlaceResponse(
    val name: String,
    val id: String,
    val displayName: LocalizedText,
    val nationalPhoneNumber: String,
    val internationalPhoneNumber: String,
    val formattedAddress: String,
    val shortFormattedAddress: String,
    val location: LatLng,
    val rating: Double?,
    val googleMapsUri: String,
    val websiteUri: String,
    val photos: List<Photo>?,
    val currentOpeningHours: OpeningHours?,
    val googleMapsLinks: GoogleMapsLinks?,
    val priceRange: PriceRange?,
)

data class LocalizedText(
    val language: String,
    val text: String
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

data class OpeningHours(
    val openNow: Boolean?,
    val weekdayText: List<String>?
)

data class Photo(
    val name: String,
    val widthPx: Int,
    val heightPx: Int,
)

data class GoogleMapsLinks(
    val url: String
)

data class PriceRange(
    val startPrice: Price?,
    val endPrice: Price?
)

data class Price(
    val currencyCode: String,
    val units: String
)