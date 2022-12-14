package ru.abyzbaev.weather_app.view.weatherList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.weather_app.databinding.FragmentWeatherListRecycleItemBinding
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.view.details.OnItemClick

class WeatherListAdapter(private val dataList: List<Weather>, private val callback: OnItemClick) :
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            FragmentWeatherListRecycleItemBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            FragmentWeatherListRecycleItemBinding.bind(itemView).apply {
                cityName.text = weather.city.name
                root.setOnClickListener {
                    callback.onItemClick(weather)
                }
            }
        }
    }
}