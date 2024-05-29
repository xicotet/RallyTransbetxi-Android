package com.canolabs.rallytransbetxi.domain.entities

import androidx.compose.ui.graphics.Color
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.utils.Constants

enum class RacingCategory {
    SERIE {
        override fun getName() = R.string.serie
        override fun getApiName() = Constants.SERIE
        override fun getTabIndex() = 0
        override fun getColor(): Color = Color.Black
    },
    PROTOTYPE {
        override fun getName() = R.string.prototype
        override fun getApiName() = Constants.PROTOTYPE
        override fun getTabIndex() = 1
        override fun getColor(): Color = Color.Red
    },
    AGRIA {
        override fun getName() = R.string.agria
        override fun getApiName() = Constants.AGRIA
        override fun getTabIndex() = 2
        override fun getColor(): Color = Color.Blue
    };

    abstract fun getName(): Int
    abstract fun getApiName(): String
    abstract fun getTabIndex(): Int
    abstract fun getColor(): Color
}