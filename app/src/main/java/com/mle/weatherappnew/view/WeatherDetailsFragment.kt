package com.mle.weatherappnew.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.mle.weatherappnew.R
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.databinding.FragmentWeatherDetailsBinding
import com.mle.weatherappnew.model.model.WeatherDTO
import com.mle.weatherappnew.utils.WeatherLoader

class WeatherDetailsFragment : Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val onResponse: OnResponse =
        object : OnResponse {
            override fun onLoaded(weatherDTO: WeatherDTO) {
                showWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                Toast.makeText(requireContext(), "Failed to load", Toast.LENGTH_SHORT).show()
            }
        }

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

        weatherBundle = arguments?.getParcelable(ARG_WEATHER) ?: Weather()
        binding.mainView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val loader = WeatherLoader(onResponse, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
    }

    private fun showWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            val city = weatherBundle.city
            cityName.text = city.cityName
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            temperatureValue.text = weatherDTO.fact.temp.toString()
            feelsLikeValue.text = weatherDTO.fact.feelsLike.toString()
            weatherCondition.text = weatherDTO.fact.condition
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}