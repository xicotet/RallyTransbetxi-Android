package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import com.canolabs.rallytransbetxi.data.models.responses.Category
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CategoryTypeConverter {

    @TypeConverter
    fun fromCategory(category: Category): String {
        return Json.encodeToString(category)
    }

    @TypeConverter
    fun toCategory(categoryString: String): Category {
        return Json.decodeFromString(categoryString)
    }
}
