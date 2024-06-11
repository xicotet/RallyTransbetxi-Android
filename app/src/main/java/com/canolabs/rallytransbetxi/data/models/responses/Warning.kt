package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.Timestamp

data class Warning(
    val titleEs: String = "",
    val titleEn: String = "",
    val titleCa: String = "",
    val titleDe: String = "",
    val contentEs: String = "",
    val contentEn: String = "",
    val contentCa: String = "",
    val contentDe: String = "",
    val date: Timestamp? = null,
    val visible: Boolean = false
)