package com.example.rutubetesttask.domain

import com.example.rutubetesttask.common.Container
import com.example.rutubetesttask.data.catalog.entities.CityDataEntity
import com.example.rutubetesttask.domain.repositories.CitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val citiesRepository: CitiesRepository
) {
    fun getCities() : Flow<Container<List<CityDataEntity>>> = citiesRepository.getCities()
}