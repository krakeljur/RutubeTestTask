package com.example.rutubetesttask.domain.repositories

import com.example.rutubetesttask.common.Container
import kotlinx.coroutines.flow.Flow


interface ForecastRepository {


    fun getForecast() : Flow<Container<Float>>

    suspend fun refreshForecast(lat: Float, long: Float)

}