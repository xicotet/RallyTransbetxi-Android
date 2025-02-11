@file:UseSerializers(TimestampSerializer::class, GeoPointSerializer::class)
package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.canolabs.rallytransbetxi.data.sources.local.serializers.GeoPointSerializer
import com.canolabs.rallytransbetxi.data.sources.local.serializers.TimestampSerializer
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Entity
@Serializable
data class Stage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val acronym: String = "",
    val name: String = "",
    val distance: String = "",
    val startTime: Timestamp? = null,
    val geoPoints: List<GeoPoint>? = null
)
