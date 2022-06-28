package com.mle.weatherappnew.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mle.weatherappnew.model.Repository
import com.mle.weatherappnew.model.RepositoryLocalImpl
import java.lang.Thread.sleep

class WeatherListViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repository = RepositoryLocalImpl()
) : ViewModel() {


    fun getLiveData() = liveData


    fun getLocalWeather() = sentRequest()

    private fun sentRequest() {
        liveData.value = AppState.Loading
        Thread {
            sleep(1000L)
            liveData.postValue(AppState.Success(repository.getWeather()))
        }.start()
    }

}