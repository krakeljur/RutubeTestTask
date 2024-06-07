package com.example.rutubetesttask.data

import com.example.rutubetesttask.common.Container
import com.example.rutubetesttask.data.catalog.CatalogApi
import com.example.rutubetesttask.data.catalog.entities.CityDataEntity
import com.example.rutubetesttask.data.forecast.ForecastApi
import com.example.rutubetesttask.domain.repositories.CitiesRepository
import com.example.rutubetesttask.domain.repositories.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi,
    private val forecastApi: ForecastApi,
    private val ioDispatcher: CoroutineDispatcher
) : CitiesRepository, ForecastRepository {

    private val citiesFlow: MutableStateFlow<Container<List<CityDataEntity>>> =
        MutableStateFlow(Container.Pending)

    private val forecastFlow: MutableStateFlow<Container<Float>> =
        MutableStateFlow(Container.Pending)

    override fun getCities(): Flow<Container<List<CityDataEntity>>> = citiesFlow.asStateFlow()

    override suspend fun refreshCities(query: String) {
        withContext(ioDispatcher) {
            try {
                citiesFlow.value = Container.Pending
                val citiesGroups = catalogApi.getCities()
                    .filter { it.city.isNotBlank() && it.city.contains(query, ignoreCase = true) }
                    .sortedBy { it.city }

                citiesFlow.value = Container.Success(citiesGroups)
            } catch (e: Exception) {
                citiesFlow.value = Container.Error(e.message ?: "")
            }
        }
    }

    override fun getForecast(): Flow<Container<Float>> = forecastFlow.asStateFlow()

    override suspend fun refreshForecast(lat: Float, long: Float) {
        withContext(ioDispatcher) {
            try {
                forecastFlow.value = Container.Pending
                val temperature = forecastApi.getForecast(lat, long).main.temp
                forecastFlow.value = Container.Success(temperature)
            } catch (e: Exception) {
                forecastFlow.value = Container.Error(e.message ?: "")
            }
        }
    }


}