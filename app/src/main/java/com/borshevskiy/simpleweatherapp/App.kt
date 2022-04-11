package com.borshevskiy.simpleweatherapp

import android.app.Application
import android.content.Intent
import androidx.room.Room
import com.borshevskiy.simpleweatherapp.business.room.OpenWeatherDatabase

class App : Application() {

    companion object{
        const val APP_SETTINGS = "App settings"
        const val IS_STARTED_UP = "Is started up"
        lateinit var db: OpenWeatherDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(this, OpenWeatherDatabase::class.java, "OpenWeatherDB")
            .fallbackToDestructiveMigration().build()

        val preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        val flag = preferences.contains(IS_STARTED_UP)

        if (!flag) {
            val editor = preferences.edit()
            editor.putBoolean(IS_STARTED_UP, true)
            editor.apply()
            val intent = Intent(this, InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}