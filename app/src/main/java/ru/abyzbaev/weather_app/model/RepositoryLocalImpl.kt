package ru.abyzbaev.weather_app.model

import android.os.SystemClock
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.domain.getRussianCities
import ru.abyzbaev.weather_app.domain.getWorldCities

class RepositoryLocalImpl:RepositoryOne, RepositoryMany {
    override fun getListWeather(location: Location): List<Weather> {
        return when(location){
            Location.Russian -> { getRussianCities() }
            Location.World -> { getWorldCities() }
        }
    }
    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }
}