package ru.abyzbaev.weather_app.view.details

import ru.abyzbaev.weather_app.model.dto.WeatherDTO

fun interface OnResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}