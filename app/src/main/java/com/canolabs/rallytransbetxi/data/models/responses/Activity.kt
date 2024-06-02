package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Timestamp? = null,
    val hour: String? = null,
    val key: String? = null,
    val activity: String = "",
    val place: String? = null,
    var index: Int = 0
)