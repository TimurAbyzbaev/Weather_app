package ru.abyzbaev.weather_app.viewmodel

sealed class AppState { //sealed насленики не считаются детьми - они приемные
    data class Success(val weatherData: Any) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}