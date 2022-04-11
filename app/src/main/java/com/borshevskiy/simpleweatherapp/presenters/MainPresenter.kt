package com.borshevskiy.simpleweatherapp.presenters

import android.util.Log
import com.borshevskiy.simpleweatherapp.business.ApiProvider
import com.borshevskiy.simpleweatherapp.business.repos.MainRepo
import com.borshevskiy.simpleweatherapp.view.MainView

class MainPresenter : BasePresenter<MainView>() {

    private val repo = MainRepo(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe { response ->
            Log.d("TEST API", "presenter enabled() $response")
            viewState.displayLocation(response.cityName)
            viewState.displayCurrentData(response.weatherData)
            viewState.displayDailyData(response.weatherData.daily)
            viewState.displayHourlyData(response.weatherData.hourly)
            response.error?.let{ viewState.displayError(response.error)}
        }
    }

    fun refresh(lat: String, lon: String) {
        viewState.setLoading(true)
        repo.reloadData(lat,lon)
    }
}