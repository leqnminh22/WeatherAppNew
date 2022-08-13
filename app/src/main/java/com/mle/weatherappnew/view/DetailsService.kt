package com.mle.weatherappnew.view

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.mle.weatherappnew.BuildConfig
import com.mle.weatherappnew.model.model.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val LATITUDE_EXTRA = "LATITUDE_EXTRA"
const val LONGITUDE_EXTRA = "LONGITUDE_EXTRA"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 5000
private const val REQUEST_API_KEY = "X-Yandex-API-Key"

class DetailsService(name: String = "DetailsService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = intent.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
            if (lat == 0.0 && lon == 0.0) {
                onEmptyData()
            } else {
                loadWeather(lat.toString(), lon.toString())
            }
        } ?: run {
            onEmptyIntent()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(lat: String, lon: String) {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                    addRequestProperty(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
                }

                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = getLines(reader)
                val weatherDTO = Gson().fromJson(response, WeatherDTO::class.java)
                onResponse(weatherDTO)

            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }

        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(weatherDTO: WeatherDTO) {
        val fact = weatherDTO.fact
        if(fact == null) {
            onEmptyResponse()
        } else {
            onSuccessResponse(fact.temp, fact.feelsLike, fact.condition)
        }
    }


    private fun onSuccessResponse(temp: Int?, feelsLike: Int?, condition: String?) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_TEMP_EXTRA, temp)
        broadcastIntent.putExtra(DETAILS_FEELS_LIKE_EXTRA, feelsLike)
        broadcastIntent.putExtra(DETAILS_CONDITION_EXTRA, condition)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }
}