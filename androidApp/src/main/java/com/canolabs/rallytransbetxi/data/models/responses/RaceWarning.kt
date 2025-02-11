@file:UseSerializers(TimestampSerializer::class)
package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.canolabs.rallytransbetxi.data.sources.local.serializers.TimestampSerializer
import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Entity
@Serializable
data class RaceWarning(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contentCa: String = "",
    val contentEn: String = "",
    val contentEs: String = "",
    val contentDe: String = "",
    val timestamp: Timestamp? = null,
    val alwaysShow: Boolean = false,
    val stageId: String? = null // Nullable, only used for stage race warnings
) {
    // Override equals to compare only the relevant fields
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RaceWarning) return false

        // stageId is not relevant for comparison as it will never be the same
        return contentCa == other.contentCa &&
                contentEn == other.contentEn &&
                contentEs == other.contentEs &&
                contentDe == other.contentDe &&
                timestamp == other.timestamp &&
                alwaysShow == other.alwaysShow
    }

    // Override hashCode to be consistent with the equals method
    override fun hashCode(): Int {
        return listOfNotNull(
            contentCa,
            contentEn,
            contentEs,
            contentDe,
            timestamp,
            alwaysShow,
            stageId
        )  // Ignore null values
            .fold(0) { acc, element -> acc * 31 + element.hashCode() }
    }
}