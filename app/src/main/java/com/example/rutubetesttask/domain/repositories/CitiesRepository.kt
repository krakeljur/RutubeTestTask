package com.example.rutubetesttask.domain.repositories

import com.example.rutubetesttask.common.Container
import com.example.rutubetesttask.data.catalog.entities.CityDataEntity
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    fun getCities() : Flow<Container<List<CityDataEntity>>>

    suspend fun refreshCities(query: String)

}