package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather

class RepositoryRemoteImpl: RepositorySpecific {
    override fun getWeatherSpecific(): Weather {
        return Weather()
    }
}