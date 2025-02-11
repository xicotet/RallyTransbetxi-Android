package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Team(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val number: String = "",
    val driver: String = "",
    val codriver: String = "",
    var category: Category = Category()
)
