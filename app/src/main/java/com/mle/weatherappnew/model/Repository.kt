package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather

interface Repository {
    fun getWeather(): Weather
}