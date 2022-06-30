package com.mle.weatherappnew.model

import com.mle.weatherappnew.data.Weather

class RepositoryRemoteImpl: RepositorySpecific {
    override fun getWeatherSpecific(): Weather {
        Thread {
            Thread.sleep(2000L)
        }.start()
        return Weather()
    }
}