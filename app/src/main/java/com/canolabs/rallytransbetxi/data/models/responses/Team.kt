package com.canolabs.rallytransbetxi.data.models.responses

import com.google.firebase.firestore.PropertyName

data class Team(
    @get: PropertyName("name") val name: String = "",
    @get: PropertyName("number") val number: String = "",
    @get: PropertyName("driver") val driver: String = "",
    @get: PropertyName("codriver") val coDriver: String = "",
    @get: PropertyName("time") val time: String = "",
    var category: Category = Category()
)
