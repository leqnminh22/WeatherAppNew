package com.mle.weatherappnew.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mle.weatherappnew.R
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.databinding.FragmentWeatherListBinding
import com.mle.weatherappnew.viewmodel.AppState
import com.mle.weatherappnew.viewmodel.WeatherListViewModel
import java.time.Duration

class WeatherListFragment : Fragment() {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherListViewModel

    private val adapter = WeatherAdapter(object : OnWeatherClicked {
        override fun onCityClicked(weather: Weather) {
            val manager = activity?.supportFragmentManager
            if(manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(WeatherDetailsFragment.ARG_WEATHER, weather)
                manager.beginTransaction()
                    .add(R.id.container, WeatherDetailsFragment.newInstance(bundle))
                    .hide(this@WeatherListFragment)
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }

        }
    })

    private var isRussian = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        val observer = Observer<Any> { renderData(it as AppState) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        val weatherList = binding.citiesListRecyclerView
        weatherList.adapter = adapter
        weatherList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.floatActionBtn.visibility = View.VISIBLE

        binding.floatActionBtn.setOnClickListener {
            changeWeatherDataSet()
        }
        viewModel.getWeatherListForRussia()
    }

    private fun changeWeatherDataSet() {
        if (isRussian) {
            viewModel.getWeatherListForWorld()
            binding.floatActionBtn.apply {
                setImageResource(R.drawable.world)
            }
        } else {
            viewModel.getWeatherListForRussia()
            binding.floatActionBtn.apply {
                setImageResource(R.drawable.russia)
            }
        }
        isRussian = !isRussian
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessSpecific -> {
                binding.progressBar.visibility = View.GONE
            }
            is AppState.SuccessMultiple -> {
                binding.progressBar.visibility = View.GONE
                adapter.setData(appState.weatherList)
            }
            is AppState.Error -> {
                binding.root.SnackError("Error", Snackbar.LENGTH_SHORT,"Try again") {
                    if(isRussian){
                        viewModel.getWeatherListForRussia()
                    } else {
                        viewModel.getWeatherListForWorld()
                    }
                }
                binding.progressBar.visibility = View.GONE
            }
            AppState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun View.SnackError(msg: String, duration: Int, toAction: String, block: (v: View )-> Unit) {
        Snackbar.make(this, msg, duration).setAction(toAction, block)
    }

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}