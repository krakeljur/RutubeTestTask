package com.example.rutubetesttask.domain.entities

import com.example.rutubetesttask.data.catalog.entity.CityDataEntity

data class CitiesGroupEntity(
    val groupName: Char,
    val cities: List<CityDataEntity>
)
