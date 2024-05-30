package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GeoPointListConverter {
    @TypeConverter
    fun fromGeoPointList(value: List<GeoPoint>?): String? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<GeoPoint>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGeoPointList(value: String?): List<GeoPoint>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<GeoPoint>>() {}.type
        return gson.fromJson(value, type)
    }
}