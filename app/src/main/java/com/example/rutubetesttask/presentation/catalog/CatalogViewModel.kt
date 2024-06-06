package com.example.rutubetesttask.presentation.catalog

import androidx.lifecycle.viewModelScope
import com.example.rutubetesttask.common.base.BaseViewModel
import com.example.rutubetesttask.common.Container
import com.example.rutubetesttask.data.catalog.entity.CityDataEntity
import com.example.rutubetesttask.domain.GetCitiesUseCase
import com.example.rutubetesttask.domain.RefreshCitiesUseCase
import com.example.rutubetesttask.domain.entities.CitiesGroupEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val refreshCitiesUseCase: RefreshCitiesUseCase
) : BaseViewModel() {

    private val citiesFlow: Flow<Container<List<CityDataEntity>>> = getCitiesUseCase.getCities()

    init {
        refreshCities()
    }

    val state: Flow<CatalogState> =
        combine(loadingFlow, errorFlow, citiesFlow) { countLoading, isError, cities ->
            return@combine CatalogState(
                isLoading = countLoading > 0 || cities is Container.Pending,
                isError = isError || cities is Container.Error,
                cities = if (cities is Container.Success) cities.data else emptyList()
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CatalogState(
                isLoading = true,
                isError = false
            )
        )

    fun refreshCities(query: String = "") {
        viewModelScope.launch {
            withLoading {
                refreshCitiesUseCase.refreshCities(query)
            }
        }
    }

    data class CatalogState(
        val isLoading: Boolean,
        val isError: Boolean,
        val cities: List<CityDataEntity> = emptyList(),
    )

}