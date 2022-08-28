package com.mle.weatherappnew.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mle.weatherappnew.model.model.WeatherDTO
import com.mle.weatherappnew.model.repository.DetailsRepository
import com.mle.weatherappnew.model.repository.DetailsRepositoryImpl
import com.mle.weatherappnew.model.repository.RemoteDataSource
import com.mle.weatherappnew.utils.convertDtoToModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

private const val SERVER_ERROR = "SERVER_ERROR"
private const val REQUEST_ERROR = "REQUEST_ERROR"
private const val CORRUPTED_DATA = "CORRUPTED_DATA"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())

) : ViewModel() {

    private val callback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            detailsLiveData.postValue(AppState.Error(Throwable(e?.message ?: REQUEST_ERROR)))
        }

        override fun onResponse(call: Call, response: Response) {
            val serverResponse: String? = response.body()?.string()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        private fun checkResponse(serverResponse: String): AppState {
            val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
            val fact = weatherDTO.fact

            return if (fact.temp == null || fact.feelsLike == null || fact.condition == null) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.SuccessMultiple(convertDtoToModel(weatherDTO))
            }
        }
    }

    fun getDetailsLiveData() = detailsLiveData

    fun getWeatherFromRemoteDataSource(requestLink: String) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(requestLink, callback)

    }
}