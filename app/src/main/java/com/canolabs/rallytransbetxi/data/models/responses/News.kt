package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity
data class News(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val imageName: String = "",
    val date: Timestamp? = null,
    val number: String = ""
)