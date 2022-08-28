package com.mle.weatherappnew.model.repository

interface DetailsRepository {
    fun getWeatherDetailsFromServer(requestLink: String, callback: okhttp3.Callback)
}