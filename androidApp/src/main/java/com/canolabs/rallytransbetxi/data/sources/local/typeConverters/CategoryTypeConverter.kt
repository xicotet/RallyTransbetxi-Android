package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryTypeConverter {

    @TypeConverter
    fun fromCategory(category: Category): String {
        return Gson().toJson(category)
    }

    @TypeConverter
    fun toCategory(categoryString: String): Category {
        val type = object : TypeToken<Category>() {}.type
        return Gson().fromJson(categoryString, type)
    }
}