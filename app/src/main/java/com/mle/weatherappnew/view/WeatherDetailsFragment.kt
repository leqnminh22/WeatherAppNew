package com.mle.weatherappnew.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mle.weatherappnew.BuildConfig
import com.mle.weatherappnew.R
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.databinding.FragmentWeatherDetailsBinding
import com.mle.weatherappnew.model.model.Fact
import com.mle.weatherappnew.model.model.WeatherDTO
import com.mle.weatherappnew.utils.snackMsgError
import com.mle.weatherappnew.viewmodel.AppState
import com.mle.weatherappnew.viewmodel.DetailsViewModel
import okhttp3.*
import java.io.IOException

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA: String = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"
private const val REQUEST_API_KEY = "X-Yandex-API-Key"
private const val MAIN_LINK = "https://api.weather.yandex.ru/v2/informers?"

class WeatherDetailsFragment : Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    private val loadResultReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> displayWeather(
                    WeatherDTO(
                        Fact(
                            intent.getIntExtra(DETAILS_TEMP_EXTRA, TEMP_INVALID),
                            intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
                            intent.getStringExtra(DETAILS_CONDITION_EXTRA)
                        )
                    )
                )
            }
        }
    }


    companion object {
        const val ARG_WEATHER = "ARG_WEATHER"
        fun newInstance(bundle: Bundle) = WeatherDetailsFragment().apply {
            arguments = bundle
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    loadResultReceiver,
                    IntentFilter(DETAILS_INTENT_FILTER)
                )
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
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getDetailsLiveData().observe(viewLifecycleOwner, observer)
        requestWeather()
//        getWeather()
    }

    private fun getWeather() {
        with(binding) {

            mainView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
//            context?.let {
//                it.startService(Intent(it, DetailsService::class.java).apply {
//                    putExtra(LATITUDE_EXTRA, weatherBundle.city.lat)
//                    putExtra(LONGITUDE_EXTRA, weatherBundle.city.lon)
//                })
//            }
        }

        val client = OkHttpClient() // клиент
        val builder: Request.Builder = Request.Builder() // создать строителя запроса
        builder.header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY) // создать заголовок запроса
        builder.url(MAIN_LINK + "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}") // Формируем URl
        val request: Request = builder.build() // создаем запрос
        val call: Call = client.newCall(request) // ставим запрос в  очередь
        call.enqueue(object : Callback {
            val handler = Handler(Looper.getMainLooper())
            override fun onResponse(call: Call, response: Response) {
                val serverResponse: String? = response.body()?.string()

                if (response.isSuccessful && serverResponse != null) {
                    handler.post {
                        displayWeather(Gson().fromJson(serverResponse, WeatherDTO::class.java))
                    }
                } else {
                    Toast.makeText(requireContext(), "Response is empty", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO(PROCESS_ERROR)
            }
        })
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessSpecific -> {
                binding.progressBar.visibility = View.GONE
            }
            is AppState.SuccessMultiple -> {
                binding.mainView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                setWeather(appState.weatherList[0])
            }
            is AppState.Error -> {
                binding.root.snackMsgError("Error", Snackbar.LENGTH_SHORT, "Try again") {
                    requestWeather()
                }
                binding.progressBar.visibility = View.GONE
            }
            AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun requestWeather() {
        viewModel.getWeatherFromRemoteDataSource(MAIN_LINK + "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
    }


    private fun setWeather(weather: Weather) = with(binding) {
        val city = weatherBundle.city
        cityName.text = city.cityName
        cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lat.toString(),
            city.lon.toString()
        )
        temperatureValue.text = weather.temperature.toString()
        feelsLikeValue.text = weather.feelsLike.toString()
        weatherCondition.text = weather.condition
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        val fact = weatherDTO.fact
        val temp = fact!!.temp
        val feelsLike = fact.feelsLike
        val condition = fact.condition
        if (temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID || condition == null) {
            TODO("ERROR")
        } else {
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
    }

    override fun onDestroy() {
        _binding = null

        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultReceiver)
        }
        super.onDestroy()
    }

}