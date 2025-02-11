@file:UseSerializers(GeoPointSerializer::class)
package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.canolabs.rallytransbetxi.data.sources.local.serializers.GeoPointSerializer
import com.google.firebase.firestore.GeoPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Entity
@Serializable
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val place: GeoPoint = GeoPoint(0.0, 0.0),
    val url: String = "",
)