package com.example.rutubetesttask.domain

import com.example.rutubetesttask.domain.repositories.ForecastRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {

    fun getForecast() = forecastRepository.getForecast()
}