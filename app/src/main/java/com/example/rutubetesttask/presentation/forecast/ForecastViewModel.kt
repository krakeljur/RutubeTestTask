package com.example.rutubetesttask.presentation.forecast

import androidx.lifecycle.viewModelScope
import com.example.rutubetesttask.common.Container
import com.example.rutubetesttask.common.base.BaseViewModel
import com.example.rutubetesttask.domain.GetForecastUseCase
import com.example.rutubetesttask.domain.RefreshForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val refreshForecastUseCase: RefreshForecastUseCase
) : BaseViewModel() {

    private val temperatureFLow: Flow<Container<Float>> = getForecastUseCase.getForecast()
    private var currentLat: Float = 0f
    private var currentLong: Float = 0f

    fun init(lat: Float, long: Float) {
        currentLat = lat
        currentLong = long
        refreshForecast()
    }

    val forecastStateFlow: Flow<ForecastState> =
        combine(loadingFlow, errorFlow, temperatureFLow) { isLoading, isError, container ->
            return@combine ForecastState(
                isLoading = isLoading > 0 || (container is Container.Pending),
                isError = isError || (container is Container.Error),
                temperature = if (container is Container.Success) container.data else 0.0f
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ForecastState(
                isLoading = true,
                isError = false,
            )
        )


    fun refreshForecast() {
        viewModelScope.launch {
            withLoading {
                refreshForecastUseCase.refreshForecast(currentLat, currentLong)
            }
        }
    }

    data class ForecastState(
        val isLoading: Boolean,
        val isError: Boolean,
        val temperature: Float = 0.0f
    )

}