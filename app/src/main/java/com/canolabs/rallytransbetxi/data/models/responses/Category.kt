package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.firestore.PropertyName

data class Category(
    val categoryId: String = "",
    @get: PropertyName("name") val name: String = "",
)
