package com.canolabs.rallytransbetxi.data.sources.local.serializers

import com.google.firebase.Timestamp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        val timestampString = "${value.seconds},${value.nanoseconds}" // Store both values
        encoder.encodeString(timestampString)
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val (seconds, nanoseconds) = decoder.decodeString().split(",").map { it.toLong() }
        return Timestamp(seconds, nanoseconds.toInt()) // Preserve nanoseconds
    }
}