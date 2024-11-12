package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppSetting(
    @PrimaryKey val id: Int = 1, // Because there is only one setting
    val theme: String,
    val profile: String,
    val fontSizeFactor: Float,
    val notificationPermissionCounter: Int,
    val areWarningsCollapsed: Boolean,
    val areNewsCollapsed: Boolean,
    val areActivitiesCollapsed: Boolean,
    // val language: String, // Not used since we store it in SharedPreferences
)