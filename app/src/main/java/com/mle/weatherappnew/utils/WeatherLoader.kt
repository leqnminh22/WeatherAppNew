package com.mle.weatherappnew.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.mle.weatherappnew.BuildConfig
import com.mle.weatherappnew.model.model.WeatherDTO
import com.mle.weatherappnew.view.OnResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onResponse: OnResponse,
                    private val lat: Double,
                    private val lon: Double) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() {
        try{
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

            val handler = Handler(Looper.getMainLooper())

            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 5000
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        BuildConfig.WEATHER_API_KEY
                    )
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = getLines(reader)
                    val weatherDTO = Gson().fromJson(response, WeatherDTO::class.java)
                    handler.post {
                        onResponse.onLoaded(weatherDTO)
                    }
                } catch (e: Exception) {
                    Log.e("","Fail connection", e)
                    e.printStackTrace()
                    onResponse.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI",e)
            e.printStackTrace()
        }
    }
}