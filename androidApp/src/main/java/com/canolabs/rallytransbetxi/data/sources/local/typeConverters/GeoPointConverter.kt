package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import com.google.firebase.firestore.GeoPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GeoPointConverter {
    @TypeConverter
    fun fromGeoPoint(value: GeoPoint?): String? {
        return value?.let {
            Json.encodeToString(SerializableGeoPoint(it.latitude, it.longitude))
        }
    }

    @TypeConverter
    fun toGeoPoint(value: String?): GeoPoint? {
        return value?.let {
            val geo = Json.decodeFromString<SerializableGeoPoint>(it)
            GeoPoint(geo.latitude, geo.longitude)
        }
    }
}

@Serializable
data class SerializableGeoPoint(val latitude: Double, val longitude: Double)