package com.canolabs.rallytransbetxi.domain.entities

import com.canolabs.rallytransbetxi.R

enum class FontSizeFactor {
    SMALL {
        override fun name() = R.string.text_size_small
        override fun next() = MEDIUM
        override fun previous() = SMALL
        override fun value() = 0.75f
    },
    MEDIUM {
        override fun name() = R.string.text_size_medium
        override fun next() = LARGE
        override fun previous() = SMALL
        override fun value() = 1.0f
    },
    LARGE {
        override fun name() = R.string.text_size_large
        override fun next() = LARGE
        override fun previous() = MEDIUM
        override fun value() = 1.5f
    };

    abstract fun name(): Int
    abstract fun next(): FontSizeFactor
    abstract fun previous(): FontSizeFactor
    abstract fun value(): Float
}