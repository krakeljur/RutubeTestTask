package com.example.rutubetesttask.domain.repositories

import com.example.rutubetesttask.base.Container
import com.example.rutubetesttask.domain.entities.CitiesGroupEntity
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    fun getCities() : Flow<Container<List<CitiesGroupEntity>>>

    suspend fun refreshCities()

}