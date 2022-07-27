package com.mle.weatherappnew.model.model


import com.google.gson.annotations.SerializedName

data class Fact(
    @SerializedName("condition")
    val condition: String,
    @SerializedName("feels_like")
    val feelsLike: Int,
    val temp: Int,
)