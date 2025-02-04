package com.canolabs.rallytransbetxi.data.models.responses

import com.google.gson.annotations.SerializedName

data class Directions(
    @SerializedName("type") val type: String,
    @SerializedName("bbox") val bbox: List<Double>,
    @SerializedName("features") val features: List<Feature>,
    @SerializedName("metadata") val metadata: Metadata
)

data class Feature(
    @SerializedName("bbox") val bbox: List<Double>,
    @SerializedName("type") val type: String,
    @SerializedName("properties") val properties: Properties,
    @SerializedName("geometry") val geometry: Geometry
)

data class Properties(
    @SerializedName("segments") val segments: List<Segment>,
    @SerializedName("way_points") val wayPoints: List<Int>,
    @SerializedName("summary") val summary: Summary
)

data class Segment(
    @SerializedName("distance") val distance: Double,
    @SerializedName("duration") val duration: Double,
    @SerializedName("steps") val steps: List<Step>
)

data class Step(
    @SerializedName("distance") val distance: Double,
    @SerializedName("duration") val duration: Double,
    @SerializedName("type") val type: Int,
    @SerializedName("instruction") val instruction: String,
    @SerializedName("name") val name: String,
    @SerializedName("way_points") val wayPoints: List<Int>
)

data class Summary(
    @SerializedName("distance") val distance: Double,
    @SerializedName("duration") val duration: Double
)

data class Geometry(
    @SerializedName("coordinates") val coordinates: List<List<Double>>,
    @SerializedName("type") val type: String
)

data class Metadata(
    @SerializedName("attribution") val attribution: String,
    @SerializedName("service") val service: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("query") val query: Query,
    @SerializedName("engine") val engine: Engine
)

data class Query(
    @SerializedName("coordinates") val coordinates: List<List<Double>>,
    @SerializedName("profile") val profile: String,
    @SerializedName("format") val format: String
)

data class Engine(
    @SerializedName("version") val version: String,
    @SerializedName("build_date") val buildDate: String,
    @SerializedName("graph_date") val graphDate: String
)