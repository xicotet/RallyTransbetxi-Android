package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class HallOfFame(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val year: String = "",
    val team: String = "",
    val driverCodriver: String = "",
)