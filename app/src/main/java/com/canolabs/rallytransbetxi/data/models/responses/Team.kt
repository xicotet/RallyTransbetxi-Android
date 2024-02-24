package com.canolabs.rallytransbetxi.data.models.responses


data class Team(
    val name: String = "",
    val number: String = "",
    val driver: String = "",
    val codriver: String = "",
    var category: Category = Category()
)
