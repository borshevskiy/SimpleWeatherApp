package com.borshevskiy.simpleweatherapp.business.repos

import android.util.Log
import com.borshevskiy.simpleweatherapp.business.ApiProvider
import com.borshevskiy.simpleweatherapp.business.model.WeatherDataModel
import com.borshevskiy.simpleweatherapp.business.room.WeatherDataEntity
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainRepo(api: ApiProvider) : BaseRepository<MainRepo.ServerResponse>(api) {

    private val gson = Gson()
    private val dbAccess = db.getWeatherDAO()

    data class ServerResponse(
        val cityName: String,
        val weatherData: WeatherDataModel,
        val error: Throwable? = null
    ) {

    }

    fun reloadData(lat: String, long: String) {
        Observable.zip(
            api.provideWeatherApi().getWeatherForecast(lat, long),
            api.provideGeoCodingApi().getCityByCoords(lat, long)
        ) { weatherData, geoCode -> ServerResponse(geoCode[0].name, weatherData) }
            .subscribeOn(Schedulers.io()).doOnNext {
                dbAccess.insertWeatherData(
                    WeatherDataEntity(
                        city = it.cityName,
                        data = gson.toJson(it.weatherData)
                    )
                )
            }
            .onErrorResumeNext {
                Observable.just(ServerResponse(
                    dbAccess.getWeatherData().city,
                    gson.fromJson(dbAccess.getWeatherData().data, WeatherDataModel::class.java),
                    it
                ))
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                dataEmitter.onNext(it)
            }, {
                dataEmitter.onError(it)
                Log.d("Error", "reloadData: $it")
            })
    }
}