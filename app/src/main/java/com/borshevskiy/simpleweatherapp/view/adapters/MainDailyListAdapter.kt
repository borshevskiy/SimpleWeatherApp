package com.borshevskiy.simpleweatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.borshevskiy.simpleweatherapp.business.model.DailyWeatherModel
import com.borshevskiy.simpleweatherapp.databinding.ItemMainDailyBinding
import com.borshevskiy.simpleweatherapp.view.*

class MainDailyListAdapter : RecyclerView.Adapter<MainDailyListAdapter.DailyWeatherViewHolder>() {

    var dailyWeather: List<DailyWeatherModel> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class DailyWeatherViewHolder(val binding: ItemMainDailyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
        return DailyWeatherViewHolder(ItemMainDailyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        val item = dailyWeather[position]
        with(holder.binding){
            with(item){
                dailyDate.text = dt.toDateFormatOf(DAY_WEEK_NAME_LONG)
                dailyPop.text = pop.toPercentString(" %")
                dailyMaxTemp.text = StringBuilder().append(temp.max.toDegree()).append("°").toString()
                dailyMinTemp.text = StringBuilder().append(temp.min.toDegree()).append("°").toString()
                dailyWeatherCondition.setImageResource(weather[0].icon.provideIcon())
            }
        }
    }

    override fun getItemCount(): Int {
        return dailyWeather.size
    }
}