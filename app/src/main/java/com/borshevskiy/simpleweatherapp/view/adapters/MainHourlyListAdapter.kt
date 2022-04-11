package com.borshevskiy.simpleweatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borshevskiy.simpleweatherapp.business.model.DailyWeatherModel
import com.borshevskiy.simpleweatherapp.business.model.HourlyWeatherModel
import com.borshevskiy.simpleweatherapp.databinding.ItemMainHourlyBinding
import com.borshevskiy.simpleweatherapp.view.*

class MainHourlyListAdapter : RecyclerView.Adapter<MainHourlyListAdapter.HourlyWeatherViewHolder>() {

    var hourlyWeather: List<HourlyWeatherModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HourlyWeatherViewHolder(val binding: ItemMainHourlyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        return HourlyWeatherViewHolder(ItemMainHourlyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val item = hourlyWeather[position]
        with(holder.binding) {
            with(item) {
                hourlyTime.text = dt.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
                hourlyTemp.text = StringBuilder().append(temp.toDegree()).append("°").toString()
                hourlyWeatherCondition.setImageResource(weather[0].icon.provideIcon())
                hourlyPop.text = pop.toPercentString(" %")
            }
        }
    }

    override fun getItemCount(): Int {
        return hourlyWeather.size
    }
}