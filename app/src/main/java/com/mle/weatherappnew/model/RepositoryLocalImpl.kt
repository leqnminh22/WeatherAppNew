package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather

class RepositoryLocalImpl: Repository {
    override fun getWeather(): Weather {
        return Weather()
    }
}