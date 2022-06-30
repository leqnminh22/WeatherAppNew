package com.mle.weatherappnew.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(val city: City = getDefaultCity(), val temperature: Int = 23, val feelsLike: Int = 20) :
    Parcelable

fun getDefaultCity() = City("Москва", 55.755826,37.617299900000035)