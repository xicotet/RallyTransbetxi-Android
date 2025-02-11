package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String = "",
    val name: String = ""
)
