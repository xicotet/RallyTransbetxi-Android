package com.canolabs.rallytransbetxi.data.models.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppSetting(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @PrimaryKey val id: Int = 1, // Because there is only one setting
    val language: String,
    val theme: String,
    val profile: String,
    val fontSizeFactor: Float
)