package com.mle.weatherappnew.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.mle.weatherappnew.BuildConfig
import com.mle.weatherappnew.model.model.WeatherDTO
import com.mle.weatherappnew.view.OnResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object WeatherLoader {

    @RequiresApi(Build.VERSION_CODES.N)
    fun request(lat: Double, lon: Double, onResponse: OnResponse) {
        val url =
            URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        var urlConnection: HttpURLConnection? = null

        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.readTimeout = 5000
        urlConnection.addRequestProperty(
            "X-Yandex-API-Key",
            BuildConfig.WEATHER_API_KEY
        )
        Thread {
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
            onResponse.onResponse(weatherDTO)
        }.start()
    }
}