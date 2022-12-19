package ru.abyzbaev.weather_app

import ru.abyzbaev.weather_app.domain.Weather

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}