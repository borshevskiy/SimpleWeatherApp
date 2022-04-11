package com.borshevskiy.simpleweatherapp.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.borshevskiy.simpleweatherapp.business.model.HourlyWeatherModel

object HourlyWeatherDiffCallback: DiffUtil.ItemCallback<HourlyWeatherModel>() {

    override fun areItemsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel)
    =  oldItem.weather == newItem.weather

    override fun areContentsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel)
    = oldItem == newItem
}