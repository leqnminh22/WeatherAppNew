package com.mle.weatherappnew.view

import com.mle.weatherappnew.model.model.WeatherDTO

fun interface OnResponse {
    fun onResponse(weather: WeatherDTO)
}