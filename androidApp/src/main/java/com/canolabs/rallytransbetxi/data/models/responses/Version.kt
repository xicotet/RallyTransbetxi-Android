package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity
data class Version(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    val timestamp: Timestamp? = null
)