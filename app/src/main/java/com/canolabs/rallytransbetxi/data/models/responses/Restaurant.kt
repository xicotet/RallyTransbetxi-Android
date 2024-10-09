package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.GeoPoint

@Entity
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val place: GeoPoint = GeoPoint(0.0, 0.0),
    val url: String = "",
)