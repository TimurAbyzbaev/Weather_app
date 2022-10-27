package ru.abyzbaev.weather_app.utils

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import ru.abyzbaev.weather_app.BuildConfig
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import ru.abyzbaev.weather_app.view.details.OnResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object WeatherLoader {
    fun requestV1(lat: Double, lon: Double, onResponse: OnResponse) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

        var myConnection: HttpURLConnection? = null


        myConnection = uri.openConnection() as HttpURLConnection
        myConnection.addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)

        myConnection.readTimeout = 5000
        val handler = Handler(Looper.myLooper()!!)
        Thread {
            val reader = BufferedReader(InputStreamReader(myConnection.inputStream))

            val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
            onResponse.onResponse(weatherDTO)
        }.start()
    }


    fun requestV2(lat: Double, lon: Double, block: (weatherDTO: WeatherDTO) -> Unit) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

        var myConnection: HttpURLConnection? = null


        myConnection = uri.openConnection() as HttpURLConnection
        myConnection.addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)

        myConnection.readTimeout = 5000
        val handler = Handler(Looper.myLooper()!!)
        Thread {
            val reader = BufferedReader(InputStreamReader(myConnection.inputStream))

            val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
            block(weatherDTO)
        }.start()
    }

}