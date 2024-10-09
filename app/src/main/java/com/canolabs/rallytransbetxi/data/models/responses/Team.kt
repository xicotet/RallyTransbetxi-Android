package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val number: String = "",
    val driver: String = "",
    val codriver: String = "",
    val driverImage: String = "",
    val codriverImage: String = "",
    var category: Category = Category()
)
