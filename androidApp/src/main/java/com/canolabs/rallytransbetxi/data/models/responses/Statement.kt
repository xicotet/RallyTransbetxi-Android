@file:UseSerializers(TimestampSerializer::class)
package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.canolabs.rallytransbetxi.data.sources.local.serializers.TimestampSerializer
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Entity
@Serializable
data class Statement(
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