package com.mle.weatherappnew.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(val city: City = getDefaultCity(), var temperature: Int = 23, var feelsLike: Int = 20) :
    Parcelable

fun getDefaultCity() = City("Москва", 55.755826,37.617299900000035)