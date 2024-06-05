package com.example.rutubetesttask.domain

import com.example.rutubetesttask.domain.repositories.CitiesRepository
import javax.inject.Inject

class RefreshCitiesUseCase @Inject constructor(
    private val citiesRepository: CitiesRepository
) {
    suspend fun refreshCities() {
        citiesRepository.refreshCities()
    }
}