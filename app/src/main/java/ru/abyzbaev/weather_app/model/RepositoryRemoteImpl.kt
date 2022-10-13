package ru.abyzbaev.weather_app.model

import android.os.SystemClock
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.viewmodel.AppState

class RepositoryRemoteImpl:Repository {
    override fun getListWeather(): List<Weather> {
        Thread{
            SystemClock.sleep(200)
        }.start()
        return listOf(Weather())
    }

    override fun getWeather(lat: Double, lon: Double): Weather {
        Thread{
            SystemClock.sleep(300)
        }.start()
        return Weather()
    }
}