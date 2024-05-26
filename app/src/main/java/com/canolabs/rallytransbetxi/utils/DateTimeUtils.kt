package com.canolabs.rallytransbetxi.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import  java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {

    const val DEFAULT_FROM_TIME = "00:00"
    const val DEFAULT_TO_TIME = "23:59"

    private const val DATE_FORMAT = "dd/MM/yyyy"
    private const val API_DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_FORMAT = "HH:mm"

    private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    private val apiDateFormatter = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

    fun secondsToDateInSpanish(seconds: Long): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val dateString = format.format(date)
        return dateString[0].uppercaseChar() + dateString.substring(1)
    }

    fun monthOfADateInSpanish(seconds: Long): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("MMM", Locale("es", "ES"))
        val dateString = format.format(date)
        return dateString[0].uppercaseChar() + dateString.substring(1)
    }

    fun dayOfADateInSpanish(seconds: Long): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("d", Locale("es", "ES"))
        return(format.format(date))
    }

    fun secondsToDateInSpanishAbbreviated(seconds: Long): String {
        val date = Date(seconds * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("d MMM", Locale("es", "ES"))
        return format.format(date).uppercase(Locale.getDefault())
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
        targetDate.set(Constants.BEGINNING_YEAR, Constants.BEGINNING_MONTH, Constants.BEGINNING_DAY, 0, 0, 0)
        targetDate.set(Calendar.MILLISECOND, 0)
        val diffMillis = targetDate.timeInMillis - now.timeInMillis
        return diffMillis / 1000
    }

    fun getCurrentDateTime(): String {
        val currentDateTime = Calendar.getInstance().time
        return dateFormatter.format(currentDateTime) + " " + timeFormatter.format(currentDateTime)
    }

    fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance()
        return dateFormatter.format(currentDate.time)
    }

    fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance()
        return timeFormatter.format(currentTime.time)
    }

    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH)
    }

    fun getCurrentDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentHour(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getCurrentMinute(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MINUTE)
    }

    // Returns true if time2 is greater than time1
    fun strictlyBeforeTime(time1: String, time2: String): Boolean {
        val parsedTime1 = parseTime(time1)
        val parsedTime2 = parseTime(time2)
        return if (parsedTime1 != null && parsedTime2 != null) {
            parsedTime2 > parsedTime1
        } else {
            false
        }
    }

    // Returns true if date2 is greater or equals than date1
    fun beforeDate(date1: String, date2: String): Boolean {
        val parsedDate1 = parseDate(date1)
        val parsedDate2 = parseDate(date2)
        return if (parsedDate1 != null && parsedDate2 != null) {
            parsedDate2 >= parsedDate1
        } else {
            false
        }
    }

    fun addDaysToDate(date: String, days: Int): String {
        val parsedDate = dateFormatter.parse(date)
        val calendar = Calendar.getInstance()
        if (parsedDate != null) {
            calendar.time = parsedDate
        }
        calendar.add(Calendar.DATE, days)
        return dateFormatter.format(calendar.time)
    }

    fun addHoursToTime(time: String, hours: Int): String {
        val parsedTime = timeFormatter.parse(time)
        val calendar = Calendar.getInstance()
        if (parsedTime != null) {
            calendar.time = parsedTime
        }
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return timeFormatter.format(calendar.time)
    }

    fun parseDate(date: String): Long? {
        try {
            return dateFormatter.parse(date)?.time
        } catch (e: ParseException) {
            return null
        }
    }

    fun parseDateToUtcMillis(date: String): Long? {
        val parsedDate = parseDate(date) ?: return null
        return parsedDate + TimeZone.getDefault().getOffset(parsedDate)
    }

    fun parseTime(time: String): Long? {
        try {
            return timeFormatter.parse(time)?.time
        } catch (e: ParseException) {
            return null
        }
    }

    fun formatDate(date: Long): String? {
        try {
            return dateFormatter.format(Date(date))
        } catch (e: ParseException) {
            return null
        }
    }

    fun formatToApiDateStandard(date: String): String? {
        return try {
            dateFormatter.parse(date)?.let { apiDateFormatter.format(it) }
        } catch (e: ParseException) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatApiDateToCustomFormat(apiDate: String): String {
        val apiFormatter = DateTimeFormatter.ofPattern(API_DATE_FORMAT)
        val customFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        val customDate = LocalDate.parse(apiDate , apiFormatter)
        return customDate.format(customFormatter)
    }
}