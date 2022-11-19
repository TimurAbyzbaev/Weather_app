package ru.abyzbaev.weather_app.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.abyzbaev.weather_app.databinding.FragmentDetailsBinding
import ru.abyzbaev.weather_app.domain.City
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.model.dto.Fact
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import ru.abyzbaev.weather_app.service.DetailsService
import ru.abyzbaev.weather_app.service.LATITUDE_EXTRA
import ru.abyzbaev.weather_app.service.LONGITUDE_EXTRA
import ru.abyzbaev.weather_app.utils.WeatherLoader

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
const val DETAILS_CONDITION_EXTRA = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

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

    private val loadResultReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                    renderData(
                        WeatherDTO(
                            Fact(
                                intent.getIntExtra(
                                    DETAILS_TEMP_EXTRA, TEMP_INVALID
                                ),
                                intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID)
                            )
                        )
                    )
                }
                else -> TODO(PROCESS_ERROR)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        requireActivity().let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultReceiver,IntentFilter(DETAILS_INTENT_FILTER))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable(BUNDLE_WEATHER_EXTRA) ?: Weather()
        city = weather.city
        getWeather()

    }

    private fun getWeather() {
        binding.mainView.hide()
        binding.loadingLayout.show()
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(LATITUDE_EXTRA, city.lat)
                putExtra(LONGITUDE_EXTRA,city.lon)
            })
        }
    }

    private fun renderData(weatherDTO: WeatherDTO) {
        binding.mainView.show()
        binding.loadingLayout.hide()

        val fact = weatherDTO.fact
        val temp = fact!!.temp
        val feelsLike = fact.feelsLike
        if(temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID){
            TODO(PROCESS_ERROR)
        }
        else{
            binding.apply {
                cityName.text = city.name
                temperatureValue.text = temp.toString()
                feelsLikeValue.text = feelsLike.toString()
                cityCoordinates.text = "${city.lat} / ${city.lon}"
            }
        }
    }

    private fun View.show(){
        this.visibility = View.VISIBLE
    }

    private fun View.hide(){
        this.visibility = View.GONE;
    }
}
