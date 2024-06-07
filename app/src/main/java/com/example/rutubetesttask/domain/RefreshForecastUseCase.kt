package com.example.rutubetesttask.domain

import com.example.rutubetesttask.domain.repositories.ForecastRepository
import javax.inject.Inject

class RefreshForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {

    suspend fun refreshForecast(lat: Float, long: Float) {
        forecastRepository.refreshForecast(lat, long)
    }
}