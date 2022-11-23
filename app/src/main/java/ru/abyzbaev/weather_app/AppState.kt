package ru.abyzbaev.weather_app

import ru.abyzbaev.weather_app.domain.Weather

sealed class AppState { //sealed насленики не считаются детьми - они приемные
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}