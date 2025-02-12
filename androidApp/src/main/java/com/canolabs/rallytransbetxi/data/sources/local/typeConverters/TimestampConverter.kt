package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import dev.gitlive.firebase.firestore.Timestamp

class TimestampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it / 1000, (it % 1000).toInt() * 1000000) }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.seconds?.times(1000)?.plus(timestamp.nanoseconds / 1000000)
    }
}