package com.mle.weatherappnew.model.model


import com.google.gson.annotations.SerializedName

data class Fact(
    val temp: Int?,
    @SerializedName("feels_like")
    val feelsLike: Int?,
    @SerializedName("condition")
    val condition: String?
)