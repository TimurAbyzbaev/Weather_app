package ru.abyzbaev.weather_app.model.repository

import ru.abyzbaev.weather_app.domain.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}