package com.mle.weatherappnew.view

import com.mle.weatherappnew.model.model.WeatherDTO

interface OnResponse {
    fun onLoaded(weatherDTO: WeatherDTO)
    fun onFailed(throwable: Throwable)
}