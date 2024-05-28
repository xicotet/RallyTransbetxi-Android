package com.canolabs.rallytransbetxi.domain.entities

enum class Language {
    SPANISH {
        override fun getDatabaseName() = "spanish"
        override fun getLanguageName() = "Español"
        override fun getLanguageCode() = "es"
        override fun getCountryCode() = "ES"
    },
    CATALAN {
        override fun getDatabaseName() = "catalan"
        override fun getLanguageName() = "Valencià"
        override fun getLanguageCode() = "ca"
        override fun getCountryCode() = "ES"
    },
    ENGLISH {
        override fun getDatabaseName() = "english"
        override fun getLanguageName() = "English"
        override fun getLanguageCode() = "en"
        override fun getCountryCode() = "US"
    },
    GERMAN {
        override fun getDatabaseName() = "german"
        override fun getLanguageName() = "Deutsch"
        override fun getLanguageCode() = "de"
        override fun getCountryCode() = "DE"
    };

    abstract fun getDatabaseName(): String
    abstract fun getLanguageName(): String
    abstract fun getLanguageCode(): String
    abstract fun getCountryCode(): String
}