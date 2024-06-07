package com.example.rutubetesttask.data.forecast

import com.example.rutubetesttask.common.Const
import com.example.rutubetesttask.data.forecast.entities.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("data/2.5/weather")
    suspend fun getForecast(@Query("lat") latitude: Float,
                            @Query("lon") longitude: Float,
                            @Query("exclude") excl: String = "minutely,hourly",
                            @Query("units") units : String = "metric",
                            @Query("appid") apiKey: String = Const.API_KEY_WEATHER) : WeatherResponse

}