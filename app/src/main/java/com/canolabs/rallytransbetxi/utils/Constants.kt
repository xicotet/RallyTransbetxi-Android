package com.canolabs.rallytransbetxi.utils

class Constants {
    companion object {
        // Day of the event
        const val BEGINNING_DAY = 26
        const val BEGINNING_MONTH = 3 // April is 3 in Java.util.calendar
        const val BEGINNING_YEAR = 2025

        // Max elements to show in the home screen
        const val MAX_NEWS = 2
        const val MAX_ACTIVITIES = 5

        const val BETXI_LOCATION = "39.927995,-0.198889"

        // OpenRouteService
        const val DIRECTIONS_BASE_URL = "https://api.openrouteservice.org/v2/"

        // Firebase storage
        const val DRIVERS_FOLDER = "drivers/"
        const val DRIVER_IMAGE_PREFIX = "driverImage"
        const val CODRIVER_IMAGE_PREFIX = "codriverImage"
        const val DRIVER_IMAGE_EXTENSION = ".png"

        const val TEAMS_FOLDER = "teams/"
        const val TEAM_IMAGE_PREFIX = "teamImage"
        const val TEAM_IMAGE_EXTENSION = ".jpg"

        const val NEWS_FOLDER = "news/"
    }
}