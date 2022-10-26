package ru.abyzbaev.weather_app.view.details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import ru.abyzbaev.weather_app.BuildConfig
import ru.abyzbaev.weather_app.R
import ru.abyzbaev.weather_app.databinding.FragmentDetailsBinding
import ru.abyzbaev.weather_app.databinding.FragmentWeatherListBinding
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import ru.abyzbaev.weather_app.utils.getLines
import ru.abyzbaev.weather_app.view.weatherList.WeatherListAdapter
import ru.abyzbaev.weather_app.view.weatherList.WeatherListFragment
import ru.abyzbaev.weather_app.view.weatherList.WeatherListViewModel
import ru.abyzbaev.weather_app.viewmodel.AppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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
            arg.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA)?.let { weather ->

                //TODO Перенести это в другое место
                weather.let {
                    val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${it.city.lat}&lon=${it.city.lon}")

                    var myConnection: HttpURLConnection? = null


                    myConnection = uri.openConnection() as HttpURLConnection
                    myConnection.addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)

                    myConnection.readTimeout = 5000
                    val handler = Handler(Looper.myLooper()!!)
                    Thread{
                        val reader = BufferedReader(InputStreamReader(myConnection.inputStream))

                        val weatherDTO = Gson().fromJson(getLines(reader),WeatherDTO::class.java)

                        requireActivity().runOnUiThread {
                            renderData(it.apply {
                                feelsLike = weatherDTO.fact.feelsLike
                                temperature = weatherDTO.fact.temp
                            })
                        }



                        /*handler.post( object : Runnable {
                            override fun run() {
                                binding.webView.loadDataWithBaseURL()
                            }
                        })*/
                    }.start()
                }





                renderData(weather)
            }
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