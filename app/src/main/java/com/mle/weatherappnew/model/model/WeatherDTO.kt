package com.mle.weatherappnew.model.model


import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: Fact

)