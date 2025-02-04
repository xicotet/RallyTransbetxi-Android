package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson

class GeoPointConverter {
    @TypeConverter
    fun fromGeoPoint(value: GeoPoint?): String? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(value, GeoPoint::class.java)
    }

    @TypeConverter
    fun toGeoPoint(value: String?): GeoPoint? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(value, GeoPoint::class.java)
    }
}