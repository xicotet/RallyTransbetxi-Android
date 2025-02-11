package com.canolabs.rallytransbetxi.data.sources.local.serializers

import com.google.firebase.firestore.GeoPoint
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object GeoPointSerializer : KSerializer<GeoPoint> {
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
        return decoder.decodeStructure(descriptor) {
            var latitude = 0.0
            var longitude = 0.0

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> latitude = decodeDoubleElement(descriptor, 0)
                    1 -> longitude = decodeDoubleElement(descriptor, 1)
                    -1 -> break@loop
                    else -> error("Unexpected index: $index")
                }
            }

            GeoPoint(latitude, longitude)
        }
    }
}