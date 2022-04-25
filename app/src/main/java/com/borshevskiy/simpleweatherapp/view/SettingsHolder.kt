package com.borshevskiy.simpleweatherapp.view

import android.content.SharedPreferences
import androidx.annotation.IdRes
import com.borshevskiy.simpleweatherapp.R
import java.util.*
import kotlin.math.roundToInt

object SettingsHolder {

    private const val TEMP = "Temp"
    private const val WIND_SPEED = "Wind speed"
    private const val PRESSURE = "Pressure"
    private const val C = 1
    private const val F = 2
    private const val MS = 3
    private const val KMH = 4
    private const val MM_HG = 5
    private const val HPA = 6

    private lateinit var preferences: SharedPreferences
    var temp = Settings.TEMP_CELSIUS
    var windSpeed = Settings.WIND_SPEED_MS
    var pressure = Settings.PRESSURE_HPA

    fun onCreate(pref: SharedPreferences) {
        preferences = pref
        temp = getSettings(preferences.getInt(TEMP, C))
        windSpeed = getSettings(preferences.getInt(WIND_SPEED, MS))
        pressure = getSettings(preferences.getInt(PRESSURE, HPA))
    }

    fun onDestroy() {
        val editor = preferences.edit()
        editor.putInt(TEMP, temp.prefConst)
        editor.putInt(WIND_SPEED, windSpeed.prefConst)
        editor.putInt(PRESSURE, pressure.prefConst)
        editor.apply()
    }

    private fun getSettings(@IdRes id : Int) = when(id) {
        C -> Settings.TEMP_CELSIUS
        F -> Settings.TEMP_FAHRENHEIT
        MS -> Settings.WIND_SPEED_MS
        KMH -> Settings.WIND_SPEED_KMH
        MM_HG -> Settings.PRESSURE_MMHG
        HPA -> Settings.PRESSURE_HPA
        else -> throw InputMismatchException()
    }

    enum class Settings(@IdRes val checkedViewId: Int, @IdRes val measureUnitStringRes: Int, val prefConst: Int) {
        TEMP_FAHRENHEIT(R.id.degreeF, R.string.f, F) {
            override fun getValue(initValue: Double) = valueToString { (initValue - 273.15) * (9/5) + 32 }
        },
        TEMP_CELSIUS(R.id.degreeC, R.string.c, C) {
            override fun getValue(initValue: Double) = valueToString { initValue - 273.15 }
        },
        WIND_SPEED_MS(R.id.speed_ms, R.string.wind_speed_mu_ms, MS) {
            override fun getValue(initValue: Double) = valueToString { initValue }
        },
        WIND_SPEED_KMH(R.id.speed_kmh, R.string.wind_speed_mu_kmh, KMH) {
            override fun getValue(initValue: Double) = valueToString { initValue * 3.6 }
        },
        PRESSURE_MMHG(R.id.pressmmHg, R.string.pressure_mu_mmHg, MM_HG) {
            override fun getValue(initValue: Double) = valueToString { initValue / 1.33322387415 }
        },
        PRESSURE_HPA(R.id.presshPa, R.string.pressure_mu_hpa, HPA) {
            override fun getValue(initValue: Double) = valueToString { initValue }
        };

        abstract fun getValue(initValue: Double): String
        protected fun valueToString(formula: () -> Double) = formula().roundToInt().toString()
    }
}