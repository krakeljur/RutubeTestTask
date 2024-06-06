package com.example.rutubetesttask.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

open class BaseViewModel : ViewModel() {

    protected val loadingFlow = MutableStateFlow(0)
    protected val errorFlow = MutableStateFlow(false)

    protected suspend fun withLoading(block: suspend () -> Unit) {
        try {
            errorFlow.value = false
            loadingFlow.value += 1
            block()
        } catch (_: Exception) {
            errorFlow.value = true
        } finally {
            loadingFlow.value -= 1
        }
    }

}