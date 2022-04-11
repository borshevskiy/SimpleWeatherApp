package com.borshevskiy.simpleweatherapp.business

import com.borshevskiy.simpleweatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/reverse?")
    fun getCityByCoords(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = LIMIT,
        @Query("appid") appid: String  = APP_ID
    ): Observable<List<GeoCodeModel>>

    companion object {
        private const val APP_ID = "a64a5c733f9080cf1f083f9a5a59b629"
        private const val LIMIT = "10"
    }
}