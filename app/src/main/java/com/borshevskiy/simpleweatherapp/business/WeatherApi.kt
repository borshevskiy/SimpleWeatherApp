package com.borshevskiy.simpleweatherapp.business

import com.borshevskiy.simpleweatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = EXCLUDE,
        @Query("appid") appid: String  = APP_ID,
        @Query("lang") lang: String = LANG
    ): Observable<WeatherDataModel>

    companion object {
        private const val EXCLUDE = "minutely,alerts"
        private const val APP_ID = "a64a5c733f9080cf1f083f9a5a59b629"
        private const val LANG = "en"
    }
}