package com.borshevskiy.simpleweatherapp.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.borshevskiy.simpleweatherapp.business.model.GeoCodeModel

object CityListDiffCallback: DiffUtil.ItemCallback<GeoCodeModel>() {

    override fun areItemsTheSame(oldItem: GeoCodeModel, newItem: GeoCodeModel) = oldItem.lat == newItem.lat

    override fun areContentsTheSame(oldItem: GeoCodeModel, newItem: GeoCodeModel) = oldItem == newItem

}