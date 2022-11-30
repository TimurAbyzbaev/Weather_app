package ru.abyzbaev.weather_app.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import ru.abyzbaev.weather_app.AppState
import ru.abyzbaev.weather_app.R
import ru.abyzbaev.weather_app.databinding.FragmentDetailsBinding
import ru.abyzbaev.weather_app.domain.City
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.utils.hide
import ru.abyzbaev.weather_app.utils.show
import ru.abyzbaev.weather_app.utils.showSnackBar


class DetailsFragment : Fragment() {
    companion object {
        const val BUNDLE_WEATHER_EXTRA = "BUNDLE_WEATHER_EXTRA"
        fun newInstance(weather: Weather) = DetailsFragment().apply {
            arguments = Bundle().also {
                it.putParcelable("BUNDLE_WEATHER_EXTRA", weather)
            }
        }
    }

    private lateinit var city: City
    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable(BUNDLE_WEATHER_EXTRA) ?: Weather()
        city = weather.city
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherFromRemoteSource(city.lat, city.lon)
    }

    private fun renderData(appState: AppState) {
        binding.mainView.show()
        binding.loadingLayout.hide()

        when (appState) {
            is AppState.Success -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.Error -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                binding.cityName.text = "Error"
                binding.temperatureValue.text = "Error"
                binding.feelsLikeValue.text = "Error"
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload)
                ) {
                    viewModel.getWeatherFromRemoteSource(city.lat, city.lon)
                }

            }
        }
    }

    private fun setWeather(weather: Weather) {
        with(binding) {
            city.let { city ->
                cityName.text = city.name
                cityCoordinates.text = "${city.lat}/${city.lon}"
            }
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
        }
        saveCity(city, weather)
        Picasso.get()
            .load("https://www.citypng.com/public/uploads/preview/city-skyline-at-night-silhouette-free-png-11665308740amrxr6qvry.png")
            .into(header_image)
    }

    private fun saveCity(city: City, weather: Weather) {
        viewModel.saveCityToDb(Weather(city, weather.temperature, weather.feelsLike))
    }
}
