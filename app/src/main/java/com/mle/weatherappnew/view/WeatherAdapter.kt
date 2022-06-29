package com.mle.weatherappnew.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mle.weatherappnew.R
import com.mle.weatherappnew.data.Weather
import com.mle.weatherappnew.databinding.ItemCityBinding

class WeatherAdapter(private val cityClicked: OnWeatherClicked) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    fun setData(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemCityBinding.bind(itemView)
        fun bind(weather: Weather) = with(binding) {

            cityName.text = weather.city.cityName
            val weatherPosition = adapterPosition
            cardView.setOnClickListener{
                cityClicked.onCityClicked(weatherData[weatherPosition])
            }
        }
    }
}