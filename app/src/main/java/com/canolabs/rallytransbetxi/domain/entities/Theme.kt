package com.canolabs.rallytransbetxi.domain.entities

import com.canolabs.rallytransbetxi.R

enum class Theme {
    LIGHT {
        override fun getDatabaseName() = "light"
        override fun getName() = R.string.light_mode
    },
    DARK {
        override fun getDatabaseName() = "dark"
        override fun getName() = R.string.dark_mode
    },
    AUTO {
        override fun getDatabaseName() = "auto"
        override fun getName() = R.string.system_default
    };
    abstract fun getDatabaseName(): String
    abstract fun getName(): Int
}