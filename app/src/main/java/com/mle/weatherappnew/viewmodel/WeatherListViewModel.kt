package com.mle.weatherappnew.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mle.weatherappnew.model.RepositoryMultiple
import com.mle.weatherappnew.model.RepositorySpecific
import com.mle.weatherappnew.model.RepositoryLocalImpl
import com.mle.weatherappnew.model.RepositoryRemoteImpl
import com.mle.weatherappnew.utils.Location

class WeatherListViewModel(
    private val liveData: MutableLiveData<Any> = MutableLiveData()

) : ViewModel() {

    private lateinit var repositorySpecific: RepositorySpecific
    private lateinit var repositoryMultiple: RepositoryMultiple

    fun getLiveData(): LiveData<Any> {
        return liveData
    }

    fun getLocalWeather() = sentRequestSpecific()
    fun getRemoteWeather() = sentRequestSpecific()

    fun getWeatherListForRussia() {
        sentRequestMultiple(Location.Russia)
    }

    fun getWeatherListForWorld() {
        sentRequestMultiple(Location.World)
    }

    private fun sentRequestSpecific() {
        repositorySpecific = RepositoryRemoteImpl()
        liveData.value = AppState.Loading
        liveData.postValue(AppState.SuccessSpecific(repositorySpecific.getWeatherSpecific()))
    }

    private fun sentRequestMultiple(location: Location) {
        repositoryMultiple = RepositoryLocalImpl()
        liveData.value = AppState.Loading
        liveData.postValue(AppState.SuccessMultiple(repositoryMultiple.getWeatherList(location)))
    }


}