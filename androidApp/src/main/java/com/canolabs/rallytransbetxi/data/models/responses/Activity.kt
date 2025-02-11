package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String? = null, // In format dd.MM.yy
    val hour: String? = null,
    val key: String? = null,
    val activityEs: String? = null,
    val activityCa: String? = null,
    val activityDe: String? = null,
    val activityEn: String? = null,
    val place: String? = null,
    var index: Int = 0
)