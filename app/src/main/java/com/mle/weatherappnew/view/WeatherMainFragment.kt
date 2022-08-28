package com.mle.weatherappnew.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mle.weatherappnew.R
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.databinding.FragmentWeatherMainBinding
import com.mle.weatherappnew.viewmodel.AppState
import com.mle.weatherappnew.viewmodel.WeatherListViewModel

class WeatherMainFragment : Fragment() {

    private var _binding: FragmentWeatherMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = WeatherMainFragment()
    }

    private lateinit var viewModel: WeatherListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        val observer = Observer<Any> { renderData(it as AppState) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getLocalWeather()

    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
            }
            AppState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is AppState.SuccessSpecific -> {
                val weather = appState.weather
                binding.progressBar.visibility = View.GONE
                setData(weather)
            }
            is AppState.SuccessMultiple -> TODO()
        }
    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.cityName
        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}