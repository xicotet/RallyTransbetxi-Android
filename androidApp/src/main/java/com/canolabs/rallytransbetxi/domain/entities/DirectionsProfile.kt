package com.canolabs.rallytransbetxi.domain.entities

import com.canolabs.rallytransbetxi.R

enum class DirectionsProfile {
    DRIVING_CAR {
        override fun getDatabaseName() = "driving-car"
        override fun getName() = R.string.driving_car_mode
    },
    FOOT_WALKING {
        override fun getDatabaseName() = "foot-walking"
        override fun getName() = R.string.walking_mode
    };

    abstract fun getDatabaseName(): String
    abstract fun getName(): Int
}