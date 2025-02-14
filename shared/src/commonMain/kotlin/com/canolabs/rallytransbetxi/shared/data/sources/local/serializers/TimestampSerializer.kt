package com.canolabs.rallytransbetxi.shared.data.sources.local.serializers

import dev.gitlive.firebase.firestore.Timestamp
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
        val timestampString = "${value.seconds},${value.nanoseconds}"
        encoder.encodeString(timestampString)
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val raw = decoder.decodeString()

        // Extract numbers from "Timestamp(seconds=1717059601, nanoseconds=182000000)"
        val matchResult = Regex("Timestamp\\(seconds=(\\d+), nanoseconds=(\\d+)\\)").find(raw)
            ?: throw IllegalArgumentException("Invalid Timestamp format: $raw")

        val (seconds, nanoseconds) = matchResult.destructured
        return Timestamp(seconds.toLong(), nanoseconds.toInt())
    }
}
