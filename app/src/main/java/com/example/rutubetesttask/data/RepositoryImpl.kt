package com.example.rutubetesttask.data

import com.example.rutubetesttask.base.Container
import com.example.rutubetesttask.data.catalog.CatalogApi
import com.example.rutubetesttask.domain.entities.CitiesGroupEntity
import com.example.rutubetesttask.domain.repositories.CitiesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi,
//    private val forecastApi: ForecastApi,
    private val ioDispatcher: CoroutineDispatcher
) : CitiesRepository {

    private val citiesFlow: MutableStateFlow<Container<List<CitiesGroupEntity>>> =
        MutableStateFlow(Container.Pending)

    override fun getCities(): Flow<Container<List<CitiesGroupEntity>>> = citiesFlow.asStateFlow()

    override suspend fun refreshCities() {
        withContext(ioDispatcher) {
            try {

                citiesFlow.value = Container.Pending

                val citiesGroups = catalogApi.getCities()
                    .filter { it.city.isNotBlank() }
                    .sortedBy { it.city }
                    .groupBy { it.city.first() }
                    .map {(groupName, cities) ->
                        CitiesGroupEntity(groupName, cities)
                    }

                citiesFlow.value = Container.Success(citiesGroups)

            } catch (e: Exception) {
                citiesFlow.value = Container.Error(e.message ?: "Something went wrong")
            }
        }
    }


}