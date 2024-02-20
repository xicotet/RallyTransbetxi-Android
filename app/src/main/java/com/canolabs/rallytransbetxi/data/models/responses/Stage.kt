package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.firestore.GeoPoint
import java.sql.Time

data class Stage(
    val name: String,
    val distance: String,
    val startTime: Time,
    val geoPoints: List<GeoPoint>?
)
