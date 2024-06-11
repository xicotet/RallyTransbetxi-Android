package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity
data class Warning(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titleEs: String = "",
    val titleEn: String = "",
    val titleCa: String = "",
    val titleDe: String = "",
    val contentEs: String = "",
    val contentEn: String = "",
    val contentCa: String = "",
    val contentDe: String = "",
    val date: Timestamp? = null,
    var needsToBePromptedAsDialog: Boolean = false,
)