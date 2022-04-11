package com.borshevskiy.simpleweatherapp.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.borshevskiy.simpleweatherapp.business.model.DailyWeatherModel

object DailyWeatherDiffCallback: DiffUtil.ItemCallback<DailyWeatherModel>() {

    override fun areItemsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel)
    =  oldItem.weather == newItem.weather

    override fun areContentsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel)
    = oldItem == newItem
}