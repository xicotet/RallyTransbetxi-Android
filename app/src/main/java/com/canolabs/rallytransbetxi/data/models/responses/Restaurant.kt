package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.firestore.GeoPoint

data class Restaurant(
    val name: String = "",
    val place: GeoPoint = GeoPoint(0.0, 0.0),
    val url: String = "",
)