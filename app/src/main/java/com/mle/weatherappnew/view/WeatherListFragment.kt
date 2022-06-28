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
import com.mle.weatherappnew.R
import com.mle.weatherappnew.databinding.FragmentWeatherListBinding
import com.mle.weatherappnew.viewmodel.AppState
import com.mle.weatherappnew.viewmodel.WeatherListViewModel

class WeatherListFragment: Fragment() {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherListViewModel
    private val adapter = WeatherAdapter()


    private var isRussian = true

    companion object{
        fun newInstance() = WeatherListFragment()
    }

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
        val observer = Observer<Any> {renderData(it as AppState)}
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        val weatherList = binding.citiesListRecyclerView
        weatherList.adapter = adapter
        weatherList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.floatActionBtn.visibility = View.VISIBLE

        binding.floatActionBtn.setOnClickListener {
            changeWeatherDataSet()
        }
        viewModel.getWeatherListForRussia()
    }

    private fun changeWeatherDataSet() {
        if(isRussian){
            viewModel.getWeatherListForWorld()
            binding.floatActionBtn.setImageResource(R.drawable.russia)
        }
        else {
            viewModel.getWeatherListForRussia()
            binding.floatActionBtn.setImageResource(R.drawable.world)
        }
        isRussian = !isRussian
    }
    private fun renderData(appState: AppState) {
       when(appState) {
           is AppState.SuccessSpecific -> {
               binding.progressBar.visibility = View.GONE
           }
           is AppState.SuccessMultiple -> {
               binding.progressBar.visibility = View.GONE
               adapter.setData(appState.weatherList)
           }
           is AppState.Error -> {
               Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
               binding.progressBar.visibility = View.GONE
           }
           AppState.Loading -> {
               binding.progressBar.visibility = View.VISIBLE
           }
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}