package ru.abyzbaev.weather_app.model

import ru.abyzbaev.weather_app.domain.Weather

class RepositoryLocalImpl:Repository {
    override fun getListWeather(): List<Weather> {
        return listOf(Weather())
    }

    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }
}