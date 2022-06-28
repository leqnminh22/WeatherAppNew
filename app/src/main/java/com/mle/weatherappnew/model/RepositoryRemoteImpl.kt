package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.data.getRussianCities
import com.mle.weatherappnew.data.getWorldCities
import com.mle.weatherappnew.utils.Location

class RepositoryRemoteImpl: RepositorySpecific {
    override fun getWeatherSpecific(): Weather {
        Thread {
            Thread.sleep(2000L)
        }.start()
        return Weather()
    }
}