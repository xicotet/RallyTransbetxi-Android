package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.Timestamp

data class News(
    val title: String = "",
    val content: String = "",
    val imageName: String = "",
    val date: Timestamp? = null ,
    val number: String = ""
)