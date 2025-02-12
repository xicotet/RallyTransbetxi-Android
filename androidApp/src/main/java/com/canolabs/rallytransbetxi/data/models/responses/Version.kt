@file:UseSerializers(TimestampSerializer::class)

package com.canolabs.rallytransbetxi.data.models.responses
import kotlinx.serialization.UseSerializers
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import dev.gitlive.firebase.firestore.Timestamp
import com.canolabs.rallytransbetxi.data.sources.local.serializers.TimestampSerializer

@Entity
@Serializable
data class Version(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    val timestamp: Timestamp? = null
)
