package ru.abyzbaev.weather_app.view.weatherList

import android.os.Bundle
import android.os.SystemClock.sleep
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.abyzbaev.weather_app.R
import ru.abyzbaev.weather_app.databinding.FragmentWeatherListBinding
import ru.abyzbaev.weather_app.viewmodel.AppState

class WeatherListFragment : Fragment() {
    companion object {
        fun newInstance() = WeatherListFragment()
    }

    var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
    get(){
        return _binding!!
    }
    lateinit var viewModel: WeatherListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<AppState>{
            override fun onChanged(t: AppState) {
                renderData(t)

            }
        })
        viewModel.sentRequest()
    }

    private fun renderData(appState: AppState){
        when (appState){
            is AppState.Error -> {
                binding.loadingLayout.visibility = GONE
                binding.cityName.text = "Ошибка"
                binding.temperatureValue.text = "Ошибка"
                binding.feelsLikeValue.text = ""
                binding.cityCoordinates.text = ""
            }
            AppState.Loading -> {
                binding.loadingLayout.visibility = VISIBLE

                binding.cityName.text = "-"
                binding.temperatureValue.text = "-"
                binding.feelsLikeValue.text = "-"
                binding.cityCoordinates.text = "-"
            }
            is AppState.Success -> {
                val result = appState.weatherData
                binding.cityName.text = result.city.name
                binding.temperatureValue.text = result.temperature.toString()
                binding.feelsLikeValue.text = result.feelsLike.toString()
                binding.cityCoordinates.text = "${result.city.lat} / ${result.city.lon}"
                binding.loadingLayout.visibility = GONE
            }
        }
    }



}