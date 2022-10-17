package ru.abyzbaev.weather_app.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.abyzbaev.weather_app.R
import ru.abyzbaev.weather_app.databinding.FragmentDetailsBinding
import ru.abyzbaev.weather_app.databinding.FragmentWeatherListBinding
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.view.weatherList.WeatherListAdapter
import ru.abyzbaev.weather_app.view.weatherList.WeatherListFragment
import ru.abyzbaev.weather_app.view.weatherList.WeatherListViewModel
import ru.abyzbaev.weather_app.viewmodel.AppState

class DetailsFragment: Fragment() {
    companion object {
        const val BUNDLE_WEATHER_EXTRA = "BUNDLE_WEATHER_EXTRA"
        fun newInstance(weather: Weather): WeatherListFragment{
            val bundle = Bundle()
            bundle.putParcelable("BUNDLE_WEATHER_EXTRA", weather)
            val fragment = WeatherListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get(){
            return _binding!!
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val weather = arguments?.get(BUNDLE_WEATHER_EXTRA)
        val weather = (arguments?.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA))
        if(weather != null)
            renderData(weather)

    }

    private fun renderData(weather: Weather){
        binding.cityName.text = weather.city.name
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.cityCoordinates.text = "${weather.city.lat} / ${weather.city.lon}"
    }
}