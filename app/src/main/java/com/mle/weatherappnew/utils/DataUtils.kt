package com.mle.weatherappnew.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.data.getDefaultCity
import com.mle.weatherappnew.model.model.Fact
import com.mle.weatherappnew.model.model.WeatherDTO

fun convertDtoToModel (weatherDTO: WeatherDTO) : List<Weather> {
    val fact: Fact = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feelsLike!!, fact.condition!!))
}
fun View.snackMsgError(msg: String, duration: Int, toAction: String, block: (v: View)-> Unit) {
    Snackbar.make(this, msg, duration).setAction(toAction, block)
}