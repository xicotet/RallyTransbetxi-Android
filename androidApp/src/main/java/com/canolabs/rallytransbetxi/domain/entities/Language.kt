package com.canolabs.rallytransbetxi.domain.entities

import com.canolabs.rallytransbetxi.R

enum class Language {
    SPANISH {
        override fun getDatabaseName() = "spanish"
        override fun getLanguageName() = "Español"
        override fun getLanguageCode() = "es"
        override fun getCountryCode() = "ES"
        override fun flagResource() = R.drawable.flag_of_spain
    },
    CATALAN {
        override fun getDatabaseName() = "catalan"
        override fun getLanguageName() = "Valencià"
        override fun getLanguageCode() = "ca"
        override fun getCountryCode() = "ES"
        override fun flagResource() = R.drawable.flag_of_the_land_of_valencia
    },
    ENGLISH {
        override fun getDatabaseName() = "english"
        override fun getLanguageName() = "English"
        override fun getLanguageCode() = "en"
        override fun getCountryCode() = "US"
        override fun flagResource() = R.drawable.flag_of_united_kingdom
    },
    GERMAN {
        override fun getDatabaseName() = "german"
        override fun getLanguageName() = "Deutsch"
        override fun getLanguageCode() = "de"
        override fun getCountryCode() = "DE"
        override fun flagResource() = R.drawable.flag_of_germany
    };

    abstract fun getDatabaseName(): String
    abstract fun getLanguageName(): String
    abstract fun getLanguageCode(): String
    abstract fun getCountryCode(): String
    abstract fun flagResource(): Int
}