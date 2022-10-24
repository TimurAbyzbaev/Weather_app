package ru.abyzbaev.weather_app.model

import ru.abyzbaev.weather_app.domain.Weather

fun interface RepositorySingleWeather {
    fun getWeather(lat: Double, lon: Double): Weather
}

fun interface RepositoryListWeather {
    fun getListWeather(location: Location): List<Weather>
}

sealed class Location {
    object Russian : Location()
    object World : Location()
}