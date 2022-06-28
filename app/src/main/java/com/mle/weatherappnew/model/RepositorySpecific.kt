package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather

fun interface RepositorySpecific {
    fun getWeatherSpecific(): Weather
}