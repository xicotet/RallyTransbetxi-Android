package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

@Entity
data class Stage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val acronym: String = "",
    val name: String = "",
    val distance: String = "",
    val startTime: Timestamp? = null,
    val geoPoints: List<GeoPoint>? = null
)
