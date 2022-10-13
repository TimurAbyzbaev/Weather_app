package ru.abyzbaev.weather_app.model

import ru.abyzbaev.weather_app.domain.Weather

interface Repository {
    fun getListWeather():List<Weather>
    fun getWeather(lat:Double, lon: Double):Weather
}