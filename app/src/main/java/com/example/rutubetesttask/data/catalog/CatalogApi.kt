package com.example.rutubetesttask.data.catalog

import com.example.rutubetesttask.data.catalog.entities.CityDataEntity
import retrofit2.http.GET

interface CatalogApi {

    @GET("Stronger197/764f9886a1e8392ddcae2521437d5a3b/raw/65164ea1af958c75c81a7f0221bead610590448e/cities.json")
    suspend fun getCities() : List<CityDataEntity>

}