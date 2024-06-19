package com.canolabs.rallytransbetxi.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import  java.util.Date
import java.util.Locale

object DateTimeUtils {
    fun secondsToDate(seconds: Long, language: String, country: String): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat(getDateFormat(language), Locale(language, country))
        val dateString = format.format(date)
        return dateString[0].uppercaseChar() + dateString.substring(1)
    }

    private fun getDateFormat(language: String): String {
        return when (language) {
            "es" -> "EEEE d 'de' MMMM 'de' yyyy"
            "ca" -> "EEEE d MMMM 'de' yyyy"
            "en" -> "EEEE d 'of' MMMM 'of' yyyy"
            "de" -> "EEEE d. MMMM yyyy"
            else -> "EEEE d 'of' MMMM 'of' yyyy"
        }
    }

    fun monthOfADate(seconds: Long, language: String, country: String): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = if (language == "ca") {
            // Language "ca" includes a preposition "de Març", so better take spanish one
            SimpleDateFormat("MMM", Locale("es", "ES"))
        } else {
            SimpleDateFormat("MMM", Locale(language, country))
        }
        val dateString = format.format(date)
        return dateString[0].uppercaseChar() + dateString.substring(1)
    }

    fun dayOfADate(seconds: Long, language: String, country: String): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("d", Locale(language, country))
        return (format.format(date))
    }

    fun yearOfADate(seconds: Long, language: String, country: String): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("yyyy", Locale(language, country))
        return (format.format(date))
    }

    fun timestampFromFormattedDate(date: String): Long? {
        val format = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        return try {
            val parsedDate = format.parse(date)
            parsedDate?.time?.div(1000) // Convert milliseconds to seconds
        } catch (e: ParseException) {
            null
        }
    }

    fun formatTimeFromSeconds(seconds: Long): String {
        val date = Date(seconds * 1000)
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = String.format("%02d", calendar.get(Calendar.MINUTE))
        return "$hour:$minute"
    }

    fun secondsUntilStartOfEvent(): Long {
        val now = Calendar.getInstance()
        val targetDate = Calendar.getInstance()
        targetDate.set(
            Constants.BEGINNING_YEAR,
            Constants.BEGINNING_MONTH,
            Constants.BEGINNING_DAY,
            0,
            0,
            0
        )
        targetDate.set(Calendar.MILLISECOND, 0)
        val diffMillis = targetDate.timeInMillis - now.timeInMillis
        return diffMillis / 1000
    }
}