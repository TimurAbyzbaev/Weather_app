package ru.abyzbaev.weather_app.viewmodel

import ru.abyzbaev.weather_app.domain.Weather

sealed class AppState { //sealed насленики не считаются детьми - они приемные
    data class SuccessOne(val weatherData: Weather) : AppState()
    data class SuccessMulti(val weatherList: List<Weather>) : AppState()

    //data class SuccessList(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}