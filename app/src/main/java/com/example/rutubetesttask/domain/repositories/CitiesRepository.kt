package com.example.rutubetesttask.domain.repositories

import com.example.rutubetesttask.common.Container
import com.example.rutubetesttask.data.catalog.entity.CityDataEntity
import com.example.rutubetesttask.domain.entities.CitiesGroupEntity
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    fun getCities() : Flow<Container<List<CityDataEntity>>>

    suspend fun refreshCities(query: String)

}