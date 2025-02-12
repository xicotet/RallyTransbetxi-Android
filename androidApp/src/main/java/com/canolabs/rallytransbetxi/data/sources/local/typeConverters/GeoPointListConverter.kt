package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import dev.gitlive.firebase.firestore.GeoPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GeoPointListConverter {
    @TypeConverter
    fun fromGeoPointList(value: List<GeoPoint>?): String? {
        return value?.map { SerializableGeoPoint(it.latitude, it.longitude) }?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toGeoPointList(value: String?): List<GeoPoint>? {
        return value?.let {
            Json.decodeFromString<List<SerializableGeoPoint>>(it).map { GeoPoint(it.latitude, it.longitude) }
        }
    }
}