package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.data.getRussianCities
import com.mle.weatherappnew.data.getWorldCities
import com.mle.weatherappnew.utils.Location

class RepositoryLocalImpl : RepositoryMultiple {
    override fun getWeatherSpecific(): Weather {
        return Weather()
    }

    override fun getWeatherList(location: Location): List<Weather> {
        return when (location) {
            Location.Russia -> getRussianCities()
            Location.World -> getWorldCities()
        }
    }
}