package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppSetting(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @PrimaryKey val id: Int = 1, // Because there is only one setting
    // val language: String, // Not used since we store it in SharedPreferences
    val theme: String,
    val profile: String,
    val fontSizeFactor: Float
)