package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.utils.Location

interface RepositoryMultiple {
    fun getWeatherSpecific(): Weather
    fun getWeatherList(location: Location): List<Weather>
}