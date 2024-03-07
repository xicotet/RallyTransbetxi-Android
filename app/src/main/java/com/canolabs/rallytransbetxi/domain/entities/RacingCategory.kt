package com.canolabs.rallytransbetxi.domain.entities

import com.canolabs.rallytransbetxi.R

enum class RacingCategory {
    SERIE {
        override fun getName() = R.string.serie
        override fun getTabIndex() = 0
    },
    PROTOTYPE {
        override fun getName() = R.string.prototype
        override fun getTabIndex() = 1
    },
    AGRIA {
        override fun getName() = R.string.agria
        override fun getTabIndex() = 2
    };

    abstract fun getName(): Int
    abstract fun getTabIndex(): Int
}