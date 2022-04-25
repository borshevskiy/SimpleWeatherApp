package com.borshevskiy.simpleweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.borshevskiy.simpleweatherapp.databinding.ActivitySettingsBinding
import com.borshevskiy.simpleweatherapp.view.SettingsHolder
import com.google.android.material.button.MaterialButtonToggleGroup

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.innerToolbar.setNavigationOnClickListener { onBackPressed() }
        setSavedSettings()
        listOf(binding.groupTemp, binding.groupWindSpeed, binding.groupPressure).forEach {
            it.addOnButtonCheckedListener(ToggleButtonClickListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SettingsHolder.onDestroy()
    }

    private fun setSavedSettings() {
        with(binding) {
            groupWindSpeed.check(SettingsHolder.windSpeed.checkedViewId)
            groupTemp.check(SettingsHolder.temp.checkedViewId)
            groupPressure.check(SettingsHolder.pressure.checkedViewId)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }

    private object ToggleButtonClickListener: MaterialButtonToggleGroup.OnButtonCheckedListener {

        override fun onButtonChecked(
            group: MaterialButtonToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        ) {
            when (checkedId to isChecked) {
                R.id.degreeC to true -> SettingsHolder.temp = SettingsHolder.Settings.TEMP_CELSIUS
                R.id.degreeF to true -> SettingsHolder.temp = SettingsHolder.Settings.TEMP_CELSIUS
                R.id.speed_ms to true -> SettingsHolder.windSpeed = SettingsHolder.Settings.WIND_SPEED_MS
                R.id.speed_kmh to true -> SettingsHolder.windSpeed = SettingsHolder.Settings.WIND_SPEED_KMH
                R.id.pressmmHg to true -> SettingsHolder.pressure = SettingsHolder.Settings.PRESSURE_MMHG
                R.id.presshPa to true -> SettingsHolder.pressure = SettingsHolder.Settings.PRESSURE_HPA
            }
        }
    }
}