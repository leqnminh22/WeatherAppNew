package com.mle.weatherappnew.view

import com.mle.weatherappnew.data.Weather

interface OnWeatherClicked {
    fun onCityClicked(weather: Weather)
}