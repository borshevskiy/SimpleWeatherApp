package com.borshevskiy.simpleweatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.borshevskiy.simpleweatherapp.R
import com.borshevskiy.simpleweatherapp.business.model.GeoCodeModel
import com.borshevskiy.simpleweatherapp.databinding.ItemCityListsBinding
import com.google.android.material.button.MaterialButton
import java.util.*

class CityListAdapter: RecyclerView.Adapter<CityListAdapter.CityListViewHolder>() {

    lateinit var clickListener: SearchItemClickListener

    var cityList: List<GeoCodeModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CityListViewHolder(val binding: ItemCityListsBinding): RecyclerView.ViewHolder(binding.root)

    interface SearchItemClickListener {

        fun addToFavorite(item: GeoCodeModel)

        fun removeFromFavorite(item: GeoCodeModel)

        fun showWeatherIn(item: GeoCodeModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        return CityListViewHolder(ItemCityListsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
        val item = cityList[position]
        with(holder.binding){
            with(item){
                location.setOnClickListener {
                    clickListener.showWeatherIn(this)
                }
                favorite.setOnClickListener {
                    when ((it as MaterialButton).isChecked) {
                        true ->  {
                            this.isFavorite = true
                            clickListener.addToFavorite(this)
                        }
                        false -> {
                            this.isFavorite = false
                            clickListener.removeFromFavorite(this)
                        }
                    }
                }
                searchCity.text = when (Locale.getDefault().displayLanguage) {
                    "русский" -> local_names.ru?: name
                    "English" -> local_names.en?: name
                    else -> name
                }
                searchCountry.text = Locale("", country).displayName
                searchState.text = if (!state.isNullOrEmpty()) holder.itemView.context.getString(R.string.comma, state) else ""
                favorite.isChecked = isFavorite
            }
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }
}