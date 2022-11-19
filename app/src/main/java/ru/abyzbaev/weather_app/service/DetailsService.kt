package ru.abyzbaev.weather_app.service

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.abyzbaev.weather_app.BuildConfig
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import ru.abyzbaev.weather_app.utils.WeatherLoader
import ru.abyzbaev.weather_app.utils.getLines
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val REQUEST_API_KEY = "X-Yandex-API-Key"

class DetailsService(name: String = "DetailService") : IntentService(name) {

    private val broadcastIntent = Intent("DETAILS_INTENT_FILTER")

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null){
            onEmptyIntent()
        } else{
            val lat = intent.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
            if (lat == 0.0 && lon == 0.0){
                onEmptyData()
            }else{
                loadWeather(lat.toString(), lon.toString())
            }
        }
    }

    private fun loadWeather(lat: String, lon: String, block:(weatherDTO : WeatherDTO)->Unit) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

        var myConnection: HttpURLConnection? = null

        myConnection = uri.openConnection() as HttpURLConnection
        myConnection.apply {
            requestMethod = REQUEST_GET
            readTimeout = REQUEST_TIMEOUT
            addRequestProperty(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
        }

        val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
        val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
        block(weatherDTO)

    }

    private fun onEmptyData() {
        onEmptyIntent()
    }

    private fun onEmptyIntent() {
        putLoadResult("DETAILS_INTENT_EMPTY_EXTRA")//TODO добавить в DetailsFragment
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra("DETAILS_LOAD_RESULT_EXTRA", result)
    }

}