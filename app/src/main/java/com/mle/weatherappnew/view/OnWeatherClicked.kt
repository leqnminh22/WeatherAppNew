package com.mle.weatherappnew.view

import com.mle.weatherappnew.data.Weather

fun interface OnWeatherClicked {
    fun onCityClicked(weather: Weather)
}