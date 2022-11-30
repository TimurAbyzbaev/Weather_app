package ru.abyzbaev.weather_app.model.repository

import ru.abyzbaev.weather_app.model.dto.WeatherDTO

interface DetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}