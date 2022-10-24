package ru.abyzbaev.weather_app.model

import android.os.SystemClock
import ru.abyzbaev.weather_app.domain.Weather

class RepositoryRemoteImpl : RepositorySingleWeather {
    override fun getWeather(lat: Double, lon: Double): Weather {
        Thread { //это бред. Нужно вызывать через callback
            SystemClock.sleep(300)
        }.start()
        return Weather()
    }
}