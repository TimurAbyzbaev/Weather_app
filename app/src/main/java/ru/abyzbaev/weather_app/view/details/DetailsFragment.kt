package ru.abyzbaev.weather_app.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.abyzbaev.weather_app.databinding.FragmentDetailsBinding
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import ru.abyzbaev.weather_app.utils.WeatherLoader

class DetailsFragment : Fragment() {
    companion object {
        const val BUNDLE_WEATHER_EXTRA = "BUNDLE_WEATHER_EXTRA"
        fun newInstance(weather: Weather) = DetailsFragment().apply {
            arguments = Bundle().also {
                it.putParcelable("BUNDLE_WEATHER_EXTRA", weather)
            }
        }
    }


    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
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

    //TODO создать DetailsListViewModel + RepositoryRemoteImpl
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.let { arg ->
            arg.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA)
        }
        weather?.let { weather ->
            weather.let { weatherLocal ->



                WeatherLoader.requestV2(weatherLocal.city.lat, weatherLocal.city.lon) { weatherDTO ->
                    bindWeatherLocalWithDTO(weatherLocal, weatherDTO)
                }
            }
        }
    }


    private fun bindWeatherLocalWithDTO(
        weatherLocal: Weather,
        weatherDTO: WeatherDTO
    ) {
        requireActivity().runOnUiThread {
            renderData(weatherLocal.apply {
                weatherLocal.temperature = weatherDTO.fact.temp
                weatherLocal.feelsLike = weatherDTO.fact.feelsLike
            })
        }
    }

    private fun renderData(weather: Weather) {
        binding.apply {
            cityName.text = weather.city.name
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            cityCoordinates.text = "${weather.city.lat} / ${weather.city.lon}"
        }
    }
}
