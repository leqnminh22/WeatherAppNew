package com.mle.weatherappnew.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mle.weatherappnew.R
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.databinding.FragmentWeatherDetailsBinding

class WeatherDetailsFragment: Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!

    companion object{

        const val ARG_WEATHER = "ARG_WEATHER"

        fun newInstance(weather: Weather): WeatherDetailsFragment {
            val args = Bundle()
            args.putParcelable(ARG_WEATHER, weather)

            val fragment = WeatherDetailsFragment ()
            fragment.arguments = args
            return fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val weather: Weather? = arguments?.getParcelable(ARG_WEATHER)
        if(weather != null) {
            showWeather(weather)
        }
    }

    private fun showWeather(weather: Weather) {
        binding.cityName.text = weather.city.cityName
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weather.city.lat.toString(),
            weather.city.lon.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}