package com.canolabs.rallytransbetxi.data.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Directions(
    val type: String,
    val bbox: List<Double>,
    val features: List<Feature>,
    val metadata: Metadata
)

@Serializable
data class Feature(
    val bbox: List<Double>,
    val type: String,
    val properties: Properties,
    val geometry: Geometry
)

@Serializable
data class Properties(
    val segments: List<Segment>,
    @SerialName("way_points") val wayPoints: List<Int>,
    val summary: Summary
)

@Serializable
data class Segment(
    val distance: Double,
    val duration: Double,
    val steps: List<Step>
)

@Serializable
data class Step(
    val distance: Double,
    val duration: Double,
    val type: Int,
    val instruction: String,
    val name: String,
    @SerialName("way_points") val wayPoints: List<Int>
)

@Serializable
data class Summary(
    val distance: Double,
    val duration: Double
)

@Serializable
data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)

@Serializable
data class Metadata(
    val attribution: String,
    val service: String,
    val timestamp: Long,
    val query: Query,
    val engine: Engine
)

@Serializable
data class Query(
    val coordinates: List<List<Double>>,
    val profile: String,
    val format: String
)

@Serializable
data class Engine(
    val version: String,
    @SerialName("build_date") val buildDate: String,
    @SerialName("graph_date") val graphDate: String
)