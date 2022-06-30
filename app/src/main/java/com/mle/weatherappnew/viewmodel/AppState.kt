package com.mle.weatherappnew.viewmodel

import com.mle.weatherappnew.data.Weather

sealed class AppState {
    data class SuccessSpecific(val weather: Weather) : AppState()
    data class SuccessMultiple(val weatherList: List<Weather>) : AppState()
    data class Error(val e: Throwable) : AppState()
    object Loading: AppState()
}