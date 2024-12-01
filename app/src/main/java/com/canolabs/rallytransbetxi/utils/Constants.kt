package com.canolabs.rallytransbetxi.utils

class Constants {
    companion object {
        const val SPLASH_SCREEN_DURATION = 1250L
        const val BETXI_LOCATION = "39.927995,-0.198889"

        // Onboarding
        const val THRESHOLD_FOR_LARGE_IMAGE_LOADING   = 1200 * 1024 // 1.2GB in kilobytes

        // Day of the event
        const val BEGINNING_DAY = 26
        const val BEGINNING_MONTH = 3 // April is 3 in Java.util.calendar
        const val BEGINNING_YEAR = 2025

        // Default number of elements to show in the home screen
        const val DEFAULT_NEWS = 2
        const val DEFAULT_WARNINGS = 1
        const val DEFAULT_ACTIVITIES = 5

        // Categories API names
        const val SERIE = "Serie"
        const val PROTOTYPE = "Prototipo"
        const val AGRIA = "Agria"

        // Room storage
        const val DATABASE_NAME = "app_database"
        const val DEFAULT_THEME = "light"
        const val DEFAULT_PROFILE = "driving-car"
        const val DEFAULT_FONT_SIZE_FACTOR = 1.0f
        const val DEFAULT_NOTIFICATION_PERMISSION_COUNTER = 0
        const val DEFAULT_WARNINGS_COLLAPSED = true
        const val DEFAULT_NEWS_COLLAPSED = false
        const val DEFAULT_ACTIVITIES_COLLAPSED = false

        // OpenRouteService API
        const val DIRECTIONS_BASE_URL = "https://api.openrouteservice.org/v2/"

        // Google Places API
        const val PLACES_BASE_URL = "https://places.googleapis.com/v1/"
        const val PLACES_FIELD_MASK = "places.displayName,places.formattedAddress,places.rating,places.location,places.photos,places.googleMapsUri,places.websiteUri,places.currentOpeningHours,places.googleMapsLinks,places.priceRange"

        // Firebase storage
        const val DRIVERS_FOLDER = "drivers/"
        const val DRIVER_IMAGE_PREFIX = "driverImage"
        const val CODRIVER_IMAGE_PREFIX = "codriverImage"
        const val DRIVER_IMAGE_EXTENSION = ".png"

        const val TEAMS_FOLDER = "teams/"
        const val TEAM_IMAGE_PREFIX = "teamImage"
        const val TEAM_IMAGE_EXTENSION = ".jpg"

        const val SPONSORS_FOLDER = "sponsors/"
        const val SPONSORS_IMAGE_PREFIX = "sponsor"
        const val SPONSORS_IMAGE_EXTENSION = ".jpg"

        const val NEWS_FOLDER = "news/"

        // Firebase remote config
        const val MIN_VERSION_KEY = "min_version"
    }
}