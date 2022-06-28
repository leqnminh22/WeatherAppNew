package com.mle.weatherappnew.data

data class Weather(val city: City = getDefaultCity(), val temperature: Int = 23, val feelsLike: Int = 20)

fun getDefaultCity() = City("Москва", 55.755826,37.617299900000035)