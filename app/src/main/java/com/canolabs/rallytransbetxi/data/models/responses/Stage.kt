package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName

data class Stage(
    @get: PropertyName("name") val name: String = "",
    @get: PropertyName("distance") val distance: String = "",
    @get: PropertyName("startTime") val startTime: Timestamp? = null,
    @get: PropertyName("geoPoints") val geoPoints: List<GeoPoint>? = null
)
