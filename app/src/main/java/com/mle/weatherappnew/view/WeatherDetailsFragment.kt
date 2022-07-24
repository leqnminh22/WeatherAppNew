package com.mle.weatherappnew.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.mle.weatherappnew.R
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.databinding.FragmentWeatherDetailsBinding
import com.mle.weatherappnew.utils.WeatherLoader

class WeatherDetailsFragment : Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {

        const val ARG_WEATHER = "ARG_WEATHER"

        fun newInstance(bundle: Bundle) = WeatherDetailsFragment().apply {
            arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val weather = arguments?.let { arg ->
            arg.getParcelable<Weather>(ARG_WEATHER)
        }
        val handler = Handler(Looper.getMainLooper())
        weather?.let { weatherLocal ->
            WeatherLoader.request(weatherLocal.city.lat, weatherLocal.city.lon) { weatherDTO ->
                handler.post {
                    showWeather(weatherLocal.apply {
                        feelsLike = weatherDTO.fact.feelsLike
                        temperature = weatherDTO.fact.temp
                    })
                }
            }
        }
    }

    private fun showWeather(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.cityName
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weather.city.lat.toString(),
                weather.city.lon.toString()
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}