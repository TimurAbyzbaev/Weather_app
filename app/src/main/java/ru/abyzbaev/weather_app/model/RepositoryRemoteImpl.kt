package ru.abyzbaev.weather_app.model

import android.os.SystemClock
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.domain.getRussianCities
import ru.abyzbaev.weather_app.domain.getWorldCities
import ru.abyzbaev.weather_app.viewmodel.AppState

class RepositoryRemoteImpl:RepositoryOne {
     override fun getWeather(lat: Double, lon: Double): Weather {
        Thread{
            SystemClock.sleep(300)
        }.start()
        return Weather()
    }
}