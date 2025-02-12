package com.canolabs.rallytransbetxi.data.sources.local.serializers
import dev.gitlive.firebase.firestore.GeoPoint
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure

object GeoPointSerializer : KSerializer<GeoPoint> {
    private val regex = """latitude=(-?\d+\.\d+),\s*longitude=(-?\d+\.\d+)""".toRegex()

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("GeoPoint") {
            element<Double>("latitude")
            element<Double>("longitude")
        }

    override fun serialize(encoder: Encoder, value: GeoPoint) {
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.latitude)
            encodeDoubleElement(descriptor, 1, value.longitude)
        }
    }

    override fun deserialize(decoder: Decoder): GeoPoint {
        val raw = decoder.decodeString()

        val matchResult = regex.find(raw)
            ?: throw IllegalArgumentException("Invalid GeoPoint format: $raw")

        val (latitude, longitude) = matchResult.destructured
        return GeoPoint(latitude.toDouble(), longitude.toDouble())
    }
}