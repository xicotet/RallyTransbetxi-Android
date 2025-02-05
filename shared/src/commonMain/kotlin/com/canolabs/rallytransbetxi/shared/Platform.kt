package com.canolabs.rallytransbetxi.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform