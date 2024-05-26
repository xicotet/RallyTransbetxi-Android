package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.Timestamp

data class Activity(
    val date: Timestamp? = null,
    val hour: String? = null,
    val key: String? = null,
    val activity: String = "",
    val place: String? = null,
    var index: Int = 0
)