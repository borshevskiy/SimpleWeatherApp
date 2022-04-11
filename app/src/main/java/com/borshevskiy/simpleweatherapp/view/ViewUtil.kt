package com.borshevskiy.simpleweatherapp.view

import com.borshevskiy.simpleweatherapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


const val DAY_FULL_MONTH_NAME = "dd MMMM"
const val DAY_WEEK_NAME_LONG = "dd EEEE"
const val HOUR_DOUBLE_DOT_MINUTE = "HH:mm"

fun Long.toDateFormatOf(format: String): String {
    val cal = Calendar.getInstance()
    val timeZone = cal.timeZone
    val sdf = SimpleDateFormat(format,Locale.getDefault())
    sdf.timeZone = timeZone
    return sdf.format(Date(this * 1000))
}

fun Double.toDegree() = (this - 273.15).roundToInt().toString()

fun Double.toPercentString(extraPart: String = "") = (this * 100).roundToInt().toString() + extraPart

fun String.provideIcon() = when(this){
    "01d", "01n" -> R.drawable.ic_01d
    "02d", "02n" -> R.drawable.ic_02d
    "03d", "03n" -> R.drawable.ic_03d
    "04d", "04n" -> R.drawable.ic_04d
    "09d", "09n" -> R.drawable.ic_09d
    "10d", "10n" -> R.drawable.ic_10d
    "11d", "11n" -> R.drawable.ic_11d
    "13d", "13n" -> R.drawable.ic_13d
    "50d", "50n" -> R.drawable.ic_50d
    else -> R.drawable.ic_error
}

