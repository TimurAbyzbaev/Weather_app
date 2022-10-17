package ru.abyzbaev.weather_app.view.details

import ru.abyzbaev.weather_app.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}